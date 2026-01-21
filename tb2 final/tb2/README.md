# Sistema de Telefonia - RMI Manual (Trabalho 2)

Sistema distribuído de gerenciamento de telefonia implementado com **protocolo RMI manual** seguindo a especificação da **Seção 5.2 do livro texto**.

## 📋 Sobre o Projeto

Este projeto implementa um sistema cliente-servidor usando:
- **Protocolo Requisição-Resposta Manual** (não usa RMI padrão do Java)
- **Métodos**: `doOperation()`, `getRequest()`, `sendReply()`
- **Serialização JSON** (representação externa de dados com Gson)
- **Passagem por Referência** (objeto remoto no servidor)
- **Passagem por Valor** (parâmetros e resultados serializados)

## 🎯 Requisitos Implementados

✅ Protocolo requisição-resposta da Seção 5.2  
✅ Métodos doOperation, getRequest, sendReply  
✅ Empacotamento de mensagens (objectReference, methodId, arguments)  
✅ Representação externa: JSON com Google Gson  
✅ 5 classes entidades (Pessoa, Cliente, Funcionario, Linha, Chamada, Fatura)  
✅ 2 composições por agregação ("tem-um")  
✅ 2 composições por extensão ("é-um")  
✅ 10 métodos para invocação remota  
✅ Passagem por referência e por valor  

## 🏗️ Arquitetura

```
┌─────────────┐                           ┌─────────────┐
│   Cliente   │                           │  Servidor   │
│  RMI Manual │                           │  RMI Manual │
└──────┬──────┘                           └──────┬──────┘
       │                                         │
       │  1. doOperation(objectRef,              │
       │     methodId, arguments)                │
       ├────────────────────────────────────────>│
       │                                         │
       │                                   2. getRequest()
       │                                         │
       │                                   3. Invocar método
       │                                      no objeto remoto
       │                                         │
       │                                   4. sendReply()
       │<────────────────────────────────────────┤
       │                                         │
       │  5. Processar resultado                 │
       │                                         │
```

### Mensagem de Requisição
```
┌──────────────┬────────────┬─────────────┬──────────┬───────────┐
│ messageType  │ requestId  │ objectRef   │ methodId │ arguments │
│   (int=0)    │   (int)    │  (String)   │ (String) │  (byte[]) │
└──────────────┴────────────┴─────────────┴──────────┴───────────┘
```

### Mensagem de Resposta
```
┌──────────────┬────────────┬──────────┬─────────┬──────────────┐
│ messageType  │ requestId  │  result  │ success │ errorMessage │
│   (int=1)    │   (int)    │ (byte[]) │ (bool)  │   (String)   │
└──────────────┴────────────┴──────────┴─────────┴──────────────┘
```

## 📦 Estrutura do Projeto

```
tb2/
├── model/                          # Modelo de dados
│   ├── Pessoa.java                 # Classe base (extensão)
│   ├── Cliente.java                # Cliente "é-um" Pessoa
│   ├── Funcionario.java            # Funcionario "é-um" Pessoa
│   ├── Linha.java                  # Linha telefônica
│   ├── Chamada.java                # Registro de chamada
│   └── Fatura.java                 # Fatura de cobrança
│
├── protocol/                       # Protocolo RMI Manual
│   ├── MensagemRequest.java        # Requisição (objectRef, methodId, args)
│   ├── MensagemReply.java          # Resposta (requestId, result)
│   ├── RemoteObjectRef.java        # Referência a objeto remoto
│   └── RequestHandler.java         # doOperation, getRequest, sendReply
│
├── server/                         # Servidor
│   ├── ServidorRMI.java            # Servidor principal (porta 5000)
│   └── ServicoTelefonia.java       # Objeto remoto (passagem por referência)
│
├── client/                         # Cliente
│   └── ClienteRMI.java             # Cliente interativo
│
├── lib/                            # Bibliotecas
│   └── gson-2.10.1.jar             # Serialização JSON
│
├── compilar.bat                    # Compilar projeto
├── iniciar-servidor.bat            # Iniciar servidor
└── iniciar-cliente.bat             # Iniciar cliente
```

## 🚀 Como Usar

### 1️⃣ Compilar
```batch
compilar.bat
```
- Baixa automaticamente a biblioteca Gson
- Compila todos os arquivos .java
- Gera 22 arquivos .class

### 2️⃣ Iniciar Servidor
Em um terminal:
```batch
iniciar-servidor.bat
```
- Porta: 5000
- Objeto remoto: `ServicoTelefonia`
- Aguarda conexões de clientes

### 3️⃣ Iniciar Cliente
Em outro terminal:
```batch
iniciar-cliente.bat
```
- Conecta a: localhost:5000
- Menu interativo com 10 opções

## 📖 Funcionalidades

### Menu do Cliente
```
1  - Adicionar Cliente
2  - Remover Cliente
3  - Consultar Cliente
4  - Listar Clientes
5  - Adicionar Linha
6  - Remover Linha
7  - Registrar Chamada
8  - Gerar Fatura
9  - Listar Faturas
10 - Ver Estatísticas
0  - Sair
```

### Exemplo de Uso

#### 1. Adicionar Cliente
```
Opção: 1
Nome: João Silva
CPF: 12345678901
Telefone: 11987654321
Email: joao@email.com
```
→ Cliente adicionado com sucesso!

#### 2. Adicionar Linha
```
Opção: 5
CPF do cliente: 12345678901
Número da linha: 11987654321
```
→ Linha adicionada com sucesso!

#### 3. Registrar Chamada
```
Opção: 7
Número de origem: 11987654321
Número de destino: 11912345678
Duração (minutos): 15
```
→ Chamada registrada com sucesso!

#### 4. Gerar Fatura
```
Opção: 8
CPF do cliente: 12345678901
```
Resultado:
```
========================================
         FATURA DE TELEFONIA            
========================================
Cliente: João Silva
CPF: 12345678901
----------------------------------------
DETALHAMENTO POR LINHA:

Linha: 11987654321
  Chamadas: 1
  Minutos: 15
  Valor: R$ 7,50

----------------------------------------
Total de Chamadas: 1
Total de Minutos: 15
Valor por Minuto: R$ 0,50
VALOR TOTAL: R$ 7,50
========================================
```

## 🔧 Detalhes Técnicos

### Protocolo Requisição-Resposta

#### doOperation() - Cliente
```java
// Cliente invoca método remoto
RemoteObjectRef objetoRemoto = new RemoteObjectRef(
    InetAddress.getByName("localhost"), 
    5000, 
    "ServicoTelefonia"
);

byte[] argumentosJSON = gson.toJson(args).getBytes();
byte[] resultadoJSON = RequestHandler.doOperation(
    objetoRemoto, 
    "adicionarCliente", 
    argumentosJSON
);
```

#### getRequest() - Servidor
```java
// Servidor recebe requisição
MensagemRequest request = RequestHandler.getRequest(clientSocket);
System.out.println("objectRef: " + request.getObjectReference());
System.out.println("methodId: " + request.getMethodId());
```

#### sendReply() - Servidor
```java
// Servidor envia resposta
MensagemReply reply = new MensagemReply(requestId, resultado);
RequestHandler.sendReply(reply, clientSocket);
```

### Composições

#### Agregação: Cliente "tem-um" conjunto de Linhas
```java
public class Cliente extends Pessoa {
    private List<Linha> linhas; // Agregação
}
```

#### Extensão: Cliente "é-um" Pessoa
```java
public class Cliente extends Pessoa {
    // Herda: nome, cpf, telefone
    private String email;
}
```

### Passagem por Referência vs Valor

#### Passagem por Referência
O objeto `ServicoTelefonia` permanece no servidor:
```java
// Servidor
private static ServicoTelefonia servicoTelefonia = new ServicoTelefonia();

// Métodos são invocados NO objeto do servidor
Object resultado = servicoTelefonia.adicionarCliente(nome, cpf, telefone, email);
```

#### Passagem por Valor
Parâmetros e resultados são serializados:
```java
// Cliente serializa argumentos
Map<String, Object> args = new HashMap<>();
args.put("nome", "João");
byte[] argumentosJSON = gson.toJson(args).getBytes();

// Servidor desserializa
Map<String, Object> args = request.getArgumentsAsMap();
String nome = (String) args.get("nome");
```

## 📊 Tecnologias

- **Java SE 8+**
- **Google Gson 2.10.1** - Serialização JSON
- **Sockets TCP** - Comunicação em rede
- **Protocol Buffers Alternative**: JSON escolhido pela simplicidade

## 🎓 Conceitos Implementados

- ✅ Sistemas Distribuídos
- ✅ Protocolo Requisição-Resposta
- ✅ RMI Manual (não usa java.rmi.*)
- ✅ Serialização/Desserialização
- ✅ Cliente-Servidor
- ✅ Programação Orientada a Objetos
- ✅ Herança e Polimorfismo
- ✅ Composição (Agregação e Extensão)
- ✅ Thread-Safety (synchronized)

## 📝 Observações

1. **Não usa RMI padrão do Java**: Implementação manual do protocolo
2. **Serialização JSON**: Representação externa de dados
3. **Porta 5000**: Configurável no código
4. **Thread-safe**: Métodos sincronizados para múltiplos clientes
5. **Dados em memória**: Reiniciar servidor limpa os dados

## 👨‍💻 Desenvolvimento

Projeto desenvolvido seguindo a especificação do **Trabalho 2 - RMI** para a disciplina de Sistemas Distribuídos.

### Diferenciais da Implementação
- Protocolo manual completo (não biblioteca de alto nível)
- Empacotamento explícito de mensagens
- Serialização JSON para portabilidade
- Estrutura extensível e bem documentada
- Scripts de automação
- Documentação completa

## 📄 Documentação Adicional

- `VERIFICACAO-TRABALHO-2.md` - Checklist completo de requisitos
- `IMPLEMENTACAO-COMPLETA.md` - Detalhes de implementação
- `GUIA-RAPIDO.md` - Guia de início rápido

---

**Sistema pronto para uso! 🚀**
