/**
 * Cliente Node.js para API REST do Sistema de Telefonia
 * Trabalho 3 - Sistemas Distribuídos
 */

const axios = require('axios');
const readline = require('readline');

class ClienteTelefoniaAPI {
    constructor(baseURL = 'http://localhost:8080') {
        this.api = axios.create({
            baseURL: baseURL,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            timeout: 5000
        });
    }

    async verificarServidor() {
        try {
            const response = await this.api.get('/api/health');
            return response.status === 200;
        } catch {
            return false;
        }
    }

    async adicionarCliente(nome, cpf, telefone, email) {
        const response = await this.api.post('/api/clientes', { nome, cpf, telefone, email });
        return response.data.message;
    }

    async listarClientes() {
        const response = await this.api.get('/api/clientes');
        return response.data;
    }

    async consultarCliente(cpf) {
        const response = await this.api.get(`/api/clientes/${cpf}`);
        return response.data.message;
    }

    async removerCliente(cpf) {
        const response = await this.api.delete(`/api/clientes/${cpf}`);
        return response.data.message;
    }

    async adicionarLinha(cpf, numero) {
        const response = await this.api.post('/api/linhas', { cpf, numero });
        return response.data.message;
    }

    async removerLinha(cpf, numero) {
        const response = await this.api.delete('/api/linhas', { data: { cpf, numero } });
        return response.data.message;
    }

    async registrarChamada(origem, destino, duracao) {
        const response = await this.api.post('/api/chamadas', { origem, destino, duracao });
        return response.data.message;
    }

    async gerarFatura(cpf) {
        const response = await this.api.post('/api/faturas', { cpf });
        return response.data.message;
    }

    async listarFaturas(cpf) {
        const response = await this.api.get(`/api/faturas/${cpf}`);
        return response.data;
    }

    async obterEstatisticas() {
        const response = await this.api.get('/api/estatisticas');
        return response.data.message;
    }
}

function exibirMenu() {
    console.log('\n╔════════════════════════════════════╗');
    console.log('║     MENU PRINCIPAL (NODE.JS)       ║');
    console.log('╠════════════════════════════════════╣');
    console.log('║  1 - Adicionar Cliente             ║');
    console.log('║  2 - Remover Cliente               ║');
    console.log('║  3 - Consultar Cliente             ║');
    console.log('║  4 - Listar Clientes               ║');
    console.log('║  5 - Adicionar Linha               ║');
    console.log('║  6 - Remover Linha                 ║');
    console.log('║  7 - Registrar Chamada             ║');
    console.log('║  8 - Gerar Fatura                  ║');
    console.log('║  9 - Listar Faturas                ║');
    console.log('║ 10 - Ver Estatísticas              ║');
    console.log('║  0 - Sair                          ║');
    console.log('╚════════════════════════════════════╝');
}

async function pergunta(rl, texto) {
    return new Promise((resolve) => {
        rl.question(texto, (resposta) => resolve(resposta.trim()));
    });
}

async function main() {
    console.log('╔════════════════════════════════════════════════════════╗');
    console.log('║   CLIENTE NODE.JS - SISTEMA DE TELEFONIA REST API      ║');
    console.log('╚════════════════════════════════════════════════════════╝');

    const api = new ClienteTelefoniaAPI();
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    // Verificar conexão
    console.log('\n🔌 Verificando conexão com servidor...');
    if (!(await api.verificarServidor())) {
        console.log('❌ Erro: Servidor não está respondendo!');
        console.log('💡 Certifique-se de que o servidor Java está rodando na porta 8080');
        rl.close();
        return;
    }
    console.log('✅ Conectado ao servidor!');

    let continuar = true;

    while (continuar) {
        try {
            exibirMenu();
            const opcao = await pergunta(rl, 'Opção: ');

            switch (opcao) {
                case '1': {
                    console.log('\n=== ADICIONAR CLIENTE ===');
                    const nome = await pergunta(rl, 'Nome: ');
                    const cpf = await pergunta(rl, 'CPF: ');
                    const telefone = await pergunta(rl, 'Telefone: ');
                    const email = await pergunta(rl, 'Email: ');
                    const resultado = await api.adicionarCliente(nome, cpf, telefone, email);
                    console.log('\n📌', resultado);
                    break;
                }

                case '2': {
                    console.log('\n=== REMOVER CLIENTE ===');
                    const cpf = await pergunta(rl, 'CPF: ');
                    const resultado = await api.removerCliente(cpf);
                    console.log('\n📌', resultado);
                    break;
                }

                case '3': {
                    console.log('\n=== CONSULTAR CLIENTE ===');
                    const cpf = await pergunta(rl, 'CPF: ');
                    const resultado = await api.consultarCliente(cpf);
                    console.log('\n', resultado);
                    break;
                }

                case '4': {
                    console.log('\n=== LISTA DE CLIENTES ===');
                    const clientes = await api.listarClientes();
                    if (clientes.length > 0) {
                        clientes.forEach((cliente, i) => {
                            console.log(`${i + 1}. ${cliente}`);
                        });
                    } else {
                        console.log('Nenhum cliente cadastrado');
                    }
                    break;
                }

                case '5': {
                    console.log('\n=== ADICIONAR LINHA ===');
                    const cpf = await pergunta(rl, 'CPF do cliente: ');
                    const numero = await pergunta(rl, 'Número da linha: ');
                    const resultado = await api.adicionarLinha(cpf, numero);
                    console.log('\n📌', resultado);
                    break;
                }

                case '6': {
                    console.log('\n=== REMOVER LINHA ===');
                    const cpf = await pergunta(rl, 'CPF do cliente: ');
                    const numero = await pergunta(rl, 'Número da linha: ');
                    const resultado = await api.removerLinha(cpf, numero);
                    console.log('\n📌', resultado);
                    break;
                }

                case '7': {
                    console.log('\n=== REGISTRAR CHAMADA ===');
                    const origem = await pergunta(rl, 'Número de origem: ');
                    const destino = await pergunta(rl, 'Número de destino: ');
                    const duracao = parseInt(await pergunta(rl, 'Duração (minutos): '));
                    const resultado = await api.registrarChamada(origem, destino, duracao);
                    console.log('\n📌', resultado);
                    break;
                }

                case '8': {
                    console.log('\n=== GERAR FATURA ===');
                    const cpf = await pergunta(rl, 'CPF do cliente: ');
                    const resultado = await api.gerarFatura(cpf);
                    console.log('\n', resultado);
                    break;
                }

                case '9': {
                    console.log('\n=== LISTAR FATURAS ===');
                    const cpf = await pergunta(rl, 'CPF do cliente: ');
                    const faturas = await api.listarFaturas(cpf);
                    console.log();
                    faturas.forEach(fatura => console.log(fatura));
                    break;
                }

                case '10': {
                    console.log('\n=== ESTATÍSTICAS DO SISTEMA ===');
                    const resultado = await api.obterEstatisticas();
                    console.log('\n', resultado);
                    break;
                }

                case '0': {
                    console.log('\n👋 Encerrando cliente Node.js...');
                    continuar = false;
                    break;
                }

                default:
                    console.log('❌ Opção inválida!');
            }
        } catch (error) {
            console.error(`❌ Erro: ${error.message}`);
        }
    }

    rl.close();
}

main().catch(console.error);
