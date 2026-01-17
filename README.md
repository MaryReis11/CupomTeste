# 🚀 Java API Challenge - Cupom 💻

## Projeto API REST Java com Spring Boot.

🌐 Acesse a documentação Swagger da API: [ http://localhost:8080/swagger-ui/index.html# ]  
A documentação interativa da API está disponível e pode ser acessada diretamente no navegador ao rodar a aplicação.

---

### 🔧 **Requisitos**

- **Maven**: Para gerenciar as dependências e build do projeto.
- **Docker**: Para containerizar a aplicação.
- **Java 21**: Versão do Java para execução da aplicação.
- **Docker Compose** (opcional): Para orquestrar os containers Docker.

---

### ⚙️ **Como executar a aplicação**

1. **Clone o repositório**

Clone o repositório para sua máquina local:

```bash
git clone https://github.com/MaryReis11/CupomTeste.git
cd CupomTeste
## Executando com Maven

Para rodar a aplicação diretamente com Maven, execute:

```bash
mvn spring-boot:run
Executando com Docker (opcional)
Você também pode executar a aplicação em um container Docker. Para isso, siga os seguintes passos:

Passo 1: Certifique-se de que o Docker esteja instalado em sua máquina.

Passo 2: Construa a imagem Docker com o comando abaixo:

bash
Copiar código
docker build -t cupom-api .
Passo 3: Execute o container com o comando:

bash
Copiar código
docker run -p 8080:8080 cupom-api
Agora, você pode acessar a aplicação na URL: http://localhost:8080/swagger-ui/index.html# .

Executando com Docker Compose (opcional)
Caso queira utilizar o Docker Compose, você pode usar o arquivo docker-compose.yml para orquestrar a aplicação.

bash
Copiar código
docker-compose up
Isso irá construir e rodar o container automaticamente.

🧩 Tecnologias Utilizadas
☕ Java 21 - A versão mais recente do Java.

🔧 Spring Boot - Framework para desenvolvimento de aplicações Java.

🐳 Docker - Para containerização da aplicação.

🔍 JUnit - Framework para testes automatizados.

📄 Swagger - Para documentação interativa da API.

🏗️ Arquitetura
A API foi desenvolvida seguindo a Clean Architecture, garantindo uma boa organização do código e separação de responsabilidades. As camadas principais são:

🔍 Repository: Responsável pela interação com os dados, utilizando uma abordagem em memória para simular um banco de dados real.

📦 Model / DTOs: Contém as entidades e objetos de transferência de dados (DTOs). Representa os dados que transitam entre as camadas e as APIs.

⚙️ Service: Contém a lógica de negócios da aplicação, com regras específicas para manipulação de cupons.

🌐 Controller: Responsável por expor a API REST. Gerencia as requisições HTTP e retorna as respostas adequadas, utilizando as informações manipuladas pela camada Service.

📌 Endpoints da API
POST: http://localhost:8080/coupon
Criação de um cupom com os dados fornecidos.

Body:

json
Copiar código
{
  "code": "ABC-125",
  "description": "Iure saepe amet. Excepturi saepe inventore nam doloremque voluptatem a. Quaerat odio distinctio eos. Dolor debitis ex molestias nam quae hic suscipit odit nulla. Blanditiis ratione facilis nobis quam deserunt. Doloribus iste corrupti magni ipsum illo beatae consectetur.",
  "discountValue": 0.8,
  "expirationDate": "2026-11-04T17:14:45.180Z",
  "published": false
}
GET: http://localhost:8080/coupon/{id}
Obtém os detalhes de um cupom pelo seu ID.

Exemplo de URL:

text
Copiar código
http://localhost:8080/coupon/8010cfb3-0c29-4336-8cc6-124feaf6ac79
DELETE: http://localhost:8080/coupon/{id}
Deleta um cupom com o ID fornecido.

Exemplo de URL:

text
Copiar código
http://localhost:8080/coupon/8010cfb3-0c29-4336-8cc6-124feaf6ac79
