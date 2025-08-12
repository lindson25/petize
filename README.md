
# Petize Test API

API desenvolvida para teste técnico da **Petize**.  
Construída com **Java**, **Spring Boot** e **PostgreSQL**.

---

## 🚀 Como rodar a API localmente

### Pré-requisitos
- Java JDK instalado
- Docker e Docker Compose instalados

---

### Passo 1: Gerar o arquivo `.jar`

Na raiz do projeto, rode:

```bash
./mvnw clean package
```  
ou, se usar Maven local:

```bash
mvn clean package
```  

Isso vai gerar o arquivo `target/petize.jar`.

---

### Passo 2: Buildar a imagem Docker

Com o `.jar` gerado, rode o comando para criar a imagem:

```bash
docker build -t petize-api .
```  

---

### Passo 3: Subir os containers com Docker Compose

Por fim, rode o Docker Compose para subir a API + banco:

```bash
docker-compose up -d
```  

---

### Passo 4: Testar a API

Acesse a API em:
```
http://localhost:8080
```  

---

## 🛠 Tecnologias usadas

- Java 17+
- Spring Boot
- PostgreSQL
- Docker / Docker Compose

---

## 📚 Documentação Completa

Toda a documentação adicional está disponível na pasta [`documentation`](./documentation).  
Dê uma olhada lá para mais detalhes!  