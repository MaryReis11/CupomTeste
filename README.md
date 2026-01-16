🚀 Java API Challenge - Cupom
💻 Projeto API REST Java com Spring Boot.

🌐 Acesse a documentação Swagger da API:
A documentação interativa da API está disponível e pode ser acessada diretamente no navegador ao rodar a aplicação.

🔧 Requisitos
Maven - Para gerenciar as dependências e build do projeto.
Docker - Para containerizar a aplicação.
Java 21 - Versão do Java para execução da aplicação.
Docker Compose (opcional) - Para orquestrar os containers Docker.
⚙️ Como executar a aplicação
1. Clone o repositório
Clone o repositório para sua máquina local:

git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
3. Executando com Maven
Para rodar a aplicação diretamente com Maven, execute:

bash
Copiar
Editar
mvn spring-boot:run
4. Executando com Docker (opcional)
Você também pode executar a aplicação em um container Docker. Para isso, siga os seguintes passos:

Passo 1: Certifique-se de que o Docker esteja instalado em sua máquina.

Passo 2: Construa a imagem Docker com o comando abaixo:

bash
Copiar
Editar
docker build -t loja-virtual-api .
Passo 3: Execute o container com o comando:

bash
Copiar
Editar
docker run -p 8080:8080 loja-virtual-api
Agora, você pode acessar a aplicação na URL: http://localhost:8080.

5. Executando com Docker Compose (opcional)
Caso queira utilizar o Docker Compose, você pode usar o arquivo docker-compose.yml para orquestrar a aplicação.

bash
Copiar
Editar
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

🔍 Repository
Responsável pela interação com os dados, utilizando uma abordagem em memória para simular um banco de dados real.

📦 Model / DTOs
Contém as entidades e objetos de transferência de dados (DTOs). Representa os dados que transitam entre as camadas e as APIs.

⚙️ Service
Contém a lógica de negócios da aplicação, com regras específicas para manipulação de produtos, categorias e carrinho de compras.

🌐 Controller
Responsável por expor a API REST. Gerencia as requisições HTTP e retorna as respostas adequadas, utilizando as informações manipuladas pela camada Service.
