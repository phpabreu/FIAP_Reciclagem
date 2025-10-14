package br.com.fiap.Reciclagem.controller;

import br.com.fiap.Reciclagem.ReciclagemApplication;
import br.com.fiap.Reciclagem.model.Material;
import br.com.fiap.Reciclagem.model.User;
import br.com.fiap.Reciclagem.model.UserRole;
import br.com.fiap.Reciclagem.repository.MaterialRepository;
import br.com.fiap.Reciclagem.repository.UserRepository;
import br.com.fiap.Reciclagem.config.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;


// Configurações base do Spring Boot para Testes de Integração
@SpringBootTest(classes = ReciclagemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("prd")
@Testcontainers
public class MaterialControllerIT {

    // Componentes injetados pelo Spring Boot
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    // Repositório para interagir com o DB do Testcontainer
    @Autowired
    private UserRepository userRepository;

    // INJECAO DO REPOSITORIO DE MATERIAL
    @Autowired
    private MaterialRepository materialRepository;

    // Constantes para o usuario de teste
    private static final String TEST_EMAIL = "testejwt@fiap.com";
    private static final String TEST_PASSWORD_BCRYPT = "$2a$10$wT5Hq2tY6x5g9uT1sA9uX.Y2K5k0c5j7t5s2m8k4j0d4h7i3l0m8c2a";
    private String validJwtToken;

    // 1. Configura e inicia o container MySQL (Testcontainers)
    @Container
// usa a imagem oficial da Microsoft para SQL Server no Docker
    public static MSSQLServerContainer<?> sqlServerContainer = new MSSQLServerContainer<>(
            "mcr.microsoft.com/mssql/server:2019-latest"
    )
            // SQL Server exige senha forte (mínimo 8 caracteres, complexa)
            .withPassword("@Fiap2025@")
            // Requisito do Docker para aceitar o Contrato de Licença de Usuário Final (EULA)
            .withEnv("ACCEPT_EULA", "Y");

    // 2. Sobrescreve as propriedades de conexão do Spring com as do Container
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        // Adiciona o driver SQL Server para o Hibernate (essencial para o dialeto)
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.SQLServerDialect");

        // Configurações do Banco de Dados usando o Testcontainer do SQL Server
        registry.add("spring.datasource.url", sqlServerContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlServerContainer::getUsername);
        registry.add("spring.datasource.password", sqlServerContainer::getPassword);

        // Garante que o Hibernate crie e drope as tabelas a cada execução de teste
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        // 3. Configuração da Secret JWT para Teste
        registry.add("jwt.secret", () -> "chave_secreta_para_teste_minimo_32_chars_123456");
    }


    //  Roda antes de CADA teste. Cria o usuário no DB de teste e gera o token JWT valido.

    @BeforeEach
    void setupAuthentication() {
        // Limpa AMBAS as tabelas para garantir o isolamento total.
        materialRepository.deleteAll(); // Limpa dados da tabela material
        userRepository.deleteAll();     // Limpa dados da tabela usuário (necessário para o setup)

        // 1. CRIA E SALVA O USUÁRIO NO DB DE TESTE
        User user = new User();
        user.setName("Teste IT");
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD_BCRYPT);
        user.setRole(UserRole.ADMIN);

        User savedUser = userRepository.save(user);

        // 2. GERA O TOKEN PARA O USUÁRIO RECÉM-CRIADO (O filtro de segurança o encontrará no DB)
        this.validJwtToken = tokenService.generateToken(savedUser);
    }

    // Helper para retornar o token JWT gerado no setup.
    private String getValidJwtToken() {
        return this.validJwtToken;
    }


    // --- TESTE DE CRIAÇÃO (POST /material/register) ---

    @Test
    @DisplayName("POST /material/register deve criar material com token e retornar 201 CREATED")
    void criarMaterial_Retorna201() throws Exception {
        // ARRANGE (Preparação)
        String token = getValidJwtToken();

        Material novoMaterial = new Material();
        novoMaterial.setNomeMaterial("Papel");

        // Acao: Simula uma requisição POST
        ResultActions result = mockMvc.perform(post("/material/register")
                .header("Authorization", "Bearer " + token) // Envia o JWT
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoMaterial)));

        // ASSERT (Verificação)
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMaterial").exists())
                .andExpect(jsonPath("$.nomeMaterial", is("Papel")));
    }

    // --- TESTE DE LISTAGEM (GET /material) ---

    @Test
    @DisplayName("GET /material deve retornar lista vazia (200 OK) com token válido")
    void getAllMaterials_ListaVazia() throws Exception {
        // ARRANGE
        String token = getValidJwtToken();

        // Acao: Simula a requisição GET
        ResultActions result = mockMvc.perform(get("/material")
                .header("Authorization", "Bearer " + token) // Envia o JWT
                .contentType(MediaType.APPLICATION_JSON));

        // ASSERT (Verificação)
        // Como limpamos a tabela no @BeforeEach, esperamos que a lista esteja vazia.
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
