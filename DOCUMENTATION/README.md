# Documentação API Teste Backend - Petize

### Coleção completa do Postman disponível na raiz do projeto para testes.

**ATENÇÃO: Lembre-se de criar as variáveis de ambiente no postman (base_url e jwt_token) para funcionar corretamente.
---

## Recursos

#### Lista de recursos da API

- [Registrar Usuário](#Registrar Usuário)
- [Fazer Login](#Fazer Login)
- [Criar Tarefa](#Criar Tarefa)
- [Listar Tarefas (com filtros)](#Listar Tarefas (com filtros))
- [Atualizar Status da Tarefa](#Atualizar Status da Tarefa)
- [Deletar Tarefa](#Deletar Tarefa)
- [Criar Subtarefa](#Criar Subtarefa)
- [Listar Subtarefas](#Listar Subtarefas)
- [Atualizar status da Subtarefa](#Atualizar status da Subtarefa)
- [Deletar Subtarefa](#Deletar Subtarefa)
- [Upload de Anexo](#Upload de Anexo)
- [Listar Anexos](#Listar Anexos)

---

## Endpoints

### `Registrar Usuário`

Cria um novo usuário no sistema.

![POST](assets/POST.png) &nbsp; **/api/auth/register**

**Exemplo de Corpo da Requisição:**

```json
{
  "name": "usuario",
  "email": "usuario@email.com",
  "password": "usuario"
}
```
**Exemplo de Corpo da Resposta:**

```json
{
  "name": "usuario",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwZXRpemUiLCJzdWIiOiJ1c3VhcmlvQGVtYWlsLmNvbSIsImV4cCI6MTc1NTA0NzE1Mn0.aC9_i3BfxDnuDuL6EOoSvmxouLTDsVs0KcCm0hCFlx4"
}
```

---

### `Fazer Login`

Entra com um usuário no sistema.

![POST](assets/POST.png) &nbsp; **/api/auth/login**

**Exemplo de Corpo da Requisição:**

```json
{
  "email": "usuario@email.com",
  "password": "usuario"
}
```
**Exemplo de Corpo da Resposta:**

```json
{
  "name": "usuario",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwZXRpemUiLCJzdWIiOiJ1c3VhcmlvQGVtYWlsLmNvbSIsImV4cCI6MTc1NTA0NzE1Mn0.aC9_i3BfxDnuDuL6EOoSvmxouLTDsVs0KcCm0hCFlx4"
}
```

---

### `Criar Tarefa`

Cria uma nova tarefa.

![POST](assets/POST.png) &nbsp; **/api/tasks**

**Exemplo de Corpo da Requisição:**

```json
{
  "title": "Finalizar projeto",
  "description": "Completar o backend da aplicação até quinta",
  "dueDate": "2025-08-14",
  "status": "PENDING",
  "priority": "HIGH"
}
```
**Exemplo de Corpo da Resposta:**

```json
{
  "id": 1,
  "title": "Finalizar projeto",
  "description": "Completar o backend da aplicação até quinta",
  "dueDate": "2025-08-14",
  "status": "PENDING",
  "priority": "HIGH"
}
```

---

### `Listar Tarefas (com filtros)`

Faz uma busca de todas as tarefas com filtros.

![GET](assets/GET.png) &nbsp; **/api/tasks**

**Exemplo de Corpo da Requisição:**

```URL
/api/tasks?status=PENDING&priority=HIGH&dueDate=2025-08-14 (Além de: page, size, sortby, direction)
```
**Exemplo de Corpo da Resposta:**

```json
{
  "content": [
    {
      "id": 1,
      "title": "Finalizar projeto",
      "description": "Completar o backend da aplicação até quinta",
      "dueDate": "2025-08-14",
      "status": "PENDING",
      "priority": "HIGH"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### `Atualizar Status da Tarefa`

Atualiza o status de uma tarefa.

![PATCH](assets/PATCH.png) &nbsp; **/api/tasks/{taskId}/status**

**Exemplo de Corpo da Requisição:**

```json
{
  "status": "COMPLETED"
}
```
**Exemplo de Corpo da Resposta:**

```json
{
  "id": 1,
  "title": "Finalizar projeto",
  "description": "Completar o backend da aplicação até quinta",
  "dueDate": "2025-08-14",
  "status": "COMPLETED", <<<
  "priority": "HIGH"
}
```

---

### `Deletar Tarefa`

Deleta uma tarefa. (Se não houver nenhuma subtarefa pendente)

![DELETE](assets/DELETE.png) &nbsp; **/api/tasks/{taskId}**

**Exemplo de Corpo da Requisição:**

```json
Não precisa de nenhum corpo.
```
**Exemplo de Corpo da Resposta:**

```json
204 No Content
```

---

### `Criar Subtarefa`

Cria uma Subtarefa.

![POST](assets/POST.png) &nbsp; **/api/tasks/{taskId}/subtasks**

**Exemplo de Corpo da Requisição:**

```json
{
  "title": "Implementar endpoint REST"
}
```
**Exemplo de Corpo da Resposta:**

```json
{
  "id": 1,
  "title": "Implementar endpoint REST",
  "status": "PENDING"
}
```

---

### `Listar Subtarefas`

Lista subtarefas.

![GET](assets/GET.png) &nbsp; **/api/tasks/{taskId}/subtasks**

**Exemplo de Corpo da Requisição:**

```json
Não precisa de nenhum corpo.
```
**Exemplo de Corpo da Resposta:**

```json
{
  "content": [
    {
      "id": 2,
      "title": "Implementar endpoint REST",
      "status": "PENDING"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### `Atualizar Status de uma Subtarefa`

Atualiza o status de uma subtarefa.

![PATCH](assets/PATCH.png) &nbsp; **/api/tasks/{taskId}/subtasks/status**

**Exemplo de Corpo da Requisição:**

```json
{
  "status": "COMPLETED"
}
```
**Exemplo de Corpo da Resposta:**

```json
{
  "id": 1,
  "title": "Implementar endpoint REST",
  "status": "COMPLETED"
}
```

---

### `Deletar Subtarefa`

Deleta uma subtarefa.

![DELETE](assets/DELETE.png) &nbsp; **/api/tasks/{taskId}/subtasks/{subtaskId}**

**Exemplo de Corpo da Requisição:**

```json
Não precisa de nenhum corpo.
```
**Exemplo de Corpo da Resposta:**

```json
204 No Content
```

---

### `Upload de Anexo Relacionado a uma Tarefa`

Faz o upload de um anexo relacionado a uma tarefa.

![POST](assets/POST.png) &nbsp; **/api/tasks/{taskId}/attachments**

**Exemplo de Corpo da Requisição:**

```json
Multipart/form-data
```
**Exemplo de Corpo da Resposta:**

```json
201 Created
```

---

### `Listar Anexos`

Lista todos os anexos.

![GET](assets/GET.png) &nbsp; **/api/tasks/{taskId}/attachments**

**Exemplo de Corpo da Requisição:**

```json
Não precisa de nenhum corpo.
```
**Exemplo de Corpo da Resposta:**

```json
{
  "content": [
    {
      "id": 1,
      "fileName": "CV - Líndson.pdf",
      "fileType": "application/pdf",
      "fileUrl": "uploads/cf8b0f90-8081-4741-9052-915ed622f682_CV - Líndson.pdf"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 1,
  "totalPages": 1,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---