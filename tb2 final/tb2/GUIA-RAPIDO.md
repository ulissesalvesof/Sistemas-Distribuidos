# Guia Rápido - Sistema RMI de Telefonia

## 🚀 Início Rápido

### 1️⃣ Compilar
```batch
cd tb2
compilar.bat
```

### 2️⃣ Iniciar Servidor (Terminal 1)
```batch
cd tb2
iniciar-servidor.bat
```

### 3️⃣ Iniciar Cliente (Terminal 2)
```batch
cd tb2
iniciar-cliente.bat
```

## 📋 Exemplo de Teste Completo

### Passo 1: Adicionar Cliente
```
Opção: 1
Nome: Maria Silva
CPF: 11122233344
```

### Passo 2: Adicionar Linha
```
Opção: 5
CPF: 11122233344
Telefone: 11987654321
```

### Passo 3: Registrar Chamadas
```
Opção: 7
Origem: 11987654321
Destino: 11912345678
Duração: 10

(Repetir com durações variadas: 5, 15, 8, 12 minutos)
```

### Passo 4: Gerar Fatura
```
Opção: 8
CPF: 11122233344
```

### Passo 5: Ver Estatísticas
```
Opção: 10
```

## 🔧 Solução de Problemas

### Erro "Address already in use"
```batch
# Mate processos Java rodando
taskkill /F /IM java.exe
```

### Erro "Connection refused"
- Verifique se o servidor está rodando
- Confirme que a porta 1099 está liberada

### Recompilar
```batch
cd tb2
compilar.bat
```

## 📊 Estrutura do Projeto

```
tb2/
├── common/          # Interface RMI
├── server/          # Servidor
├── client/          # Cliente  
├── util/            # Utilitários
├── compilar.bat     # Compila tudo
├── iniciar-servidor.bat
├── iniciar-cliente.bat
└── README.md        # Documentação completa
```

## ⚙️ Configurações

- **Porta RMI**: 1099
- **Nome do Serviço**: ServicoTelefonia
- **Host**: localhost
- **Valor por minuto**: R$ 0,50

Para mais detalhes, consulte o [README.md](README.md) completo.
