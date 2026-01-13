#!/usr/bin/env python3
"""
Cliente Python para API REST do Sistema de Telefonia
Trabalho 3 - Sistemas Distribuídos
"""

import requests
import json
from typing import Dict, List, Optional

class ClienteTelefoniaAPI:
    """Cliente para consumir a API REST do sistema de telefonia"""
    
    def __init__(self, base_url: str = "http://localhost:8080"):
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        })
    
    def verificar_servidor(self) -> bool:
        """Verifica se o servidor está online"""
        try:
            response = self.session.get(f"{self.base_url}/api/health", timeout=2)
            return response.status_code == 200
        except:
            return False
    
    def adicionar_cliente(self, nome: str, cpf: str, telefone: str, email: str) -> str:
        """Adiciona um novo cliente"""
        dados = {
            "nome": nome,
            "cpf": cpf,
            "telefone": telefone,
            "email": email
        }
        response = self.session.post(f"{self.base_url}/api/clientes", json=dados)
        return response.json().get("message", "Erro")
    
    def listar_clientes(self) -> List[str]:
        """Lista todos os clientes"""
        response = self.session.get(f"{self.base_url}/api/clientes")
        return response.json()
    
    def consultar_cliente(self, cpf: str) -> str:
        """Consulta informações de um cliente"""
        response = self.session.get(f"{self.base_url}/api/clientes/{cpf}")
        return response.json().get("message", "Erro")
    
    def remover_cliente(self, cpf: str) -> str:
        """Remove um cliente"""
        response = self.session.delete(f"{self.base_url}/api/clientes/{cpf}")
        return response.json().get("message", "Erro")
    
    def adicionar_linha(self, cpf: str, numero: str) -> str:
        """Adiciona uma linha telefônica"""
        dados = {"cpf": cpf, "numero": numero}
        response = self.session.post(f"{self.base_url}/api/linhas", json=dados)
        return response.json().get("message", "Erro")
    
    def remover_linha(self, cpf: str, numero: str) -> str:
        """Remove uma linha telefônica"""
        dados = {"cpf": cpf, "numero": numero}
        response = self.session.request("DELETE", f"{self.base_url}/api/linhas", json=dados)
        return response.json().get("message", "Erro")
    
    def registrar_chamada(self, origem: str, destino: str, duracao: int) -> str:
        """Registra uma chamada"""
        dados = {"origem": origem, "destino": destino, "duracao": duracao}
        response = self.session.post(f"{self.base_url}/api/chamadas", json=dados)
        return response.json().get("message", "Erro")
    
    def gerar_fatura(self, cpf: str) -> str:
        """Gera fatura de um cliente"""
        dados = {"cpf": cpf}
        response = self.session.post(f"{self.base_url}/api/faturas", json=dados)
        return response.json().get("message", "Erro")
    
    def listar_faturas(self, cpf: str) -> List[str]:
        """Lista faturas de um cliente"""
        response = self.session.get(f"{self.base_url}/api/faturas/{cpf}")
        return response.json()
    
    def obter_estatisticas(self) -> str:
        """Obtém estatísticas do sistema"""
        response = self.session.get(f"{self.base_url}/api/estatisticas")
        return response.json().get("message", "Erro")


def exibir_menu():
    """Exibe o menu principal"""
    print("\n╔════════════════════════════════════╗")
    print("║      MENU PRINCIPAL (PYTHON)       ║")
    print("╠════════════════════════════════════╣")
    print("║  1 - Adicionar Cliente             ║")
    print("║  2 - Remover Cliente               ║")
    print("║  3 - Consultar Cliente             ║")
    print("║  4 - Listar Clientes               ║")
    print("║  5 - Adicionar Linha               ║")
    print("║  6 - Remover Linha                 ║")
    print("║  7 - Registrar Chamada             ║")
    print("║  8 - Gerar Fatura                  ║")
    print("║  9 - Listar Faturas                ║")
    print("║ 10 - Ver Estatísticas              ║")
    print("║  0 - Sair                          ║")
    print("╚════════════════════════════════════╝")


def main():
    print("╔════════════════════════════════════════════════════════╗")
    print("║    CLIENTE PYTHON - SISTEMA DE TELEFONIA REST API      ║")
    print("╚════════════════════════════════════════════════════════╝")
    
    api = ClienteTelefoniaAPI()
    
    # Verificar conexão
    print("\n🔌 Verificando conexão com servidor...")
    if not api.verificar_servidor():
        print("❌ Erro: Servidor não está respondendo!")
        print("💡 Certifique-se de que o servidor Java está rodando na porta 8080")
        return
    
    print("✅ Conectado ao servidor!")
    
    while True:
        try:
            exibir_menu()
            opcao = input("Opção: ").strip()
            
            if opcao == "1":
                print("\n=== ADICIONAR CLIENTE ===")
                nome = input("Nome: ")
                cpf = input("CPF: ")
                telefone = input("Telefone: ")
                email = input("Email: ")
                print("\n📌", api.adicionar_cliente(nome, cpf, telefone, email))
            
            elif opcao == "2":
                print("\n=== REMOVER CLIENTE ===")
                cpf = input("CPF: ")
                print("\n📌", api.remover_cliente(cpf))
            
            elif opcao == "3":
                print("\n=== CONSULTAR CLIENTE ===")
                cpf = input("CPF: ")
                print("\n", api.consultar_cliente(cpf))
            
            elif opcao == "4":
                print("\n=== LISTA DE CLIENTES ===")
                clientes = api.listar_clientes()
                if clientes:
                    for i, cliente in enumerate(clientes, 1):
                        print(f"{i}. {cliente}")
                else:
                    print("Nenhum cliente cadastrado")
            
            elif opcao == "5":
                print("\n=== ADICIONAR LINHA ===")
                cpf = input("CPF do cliente: ")
                numero = input("Número da linha: ")
                print("\n📌", api.adicionar_linha(cpf, numero))
            
            elif opcao == "6":
                print("\n=== REMOVER LINHA ===")
                cpf = input("CPF do cliente: ")
                numero = input("Número da linha: ")
                print("\n📌", api.remover_linha(cpf, numero))
            
            elif opcao == "7":
                print("\n=== REGISTRAR CHAMADA ===")
                origem = input("Número de origem: ")
                destino = input("Número de destino: ")
                duracao = int(input("Duração (minutos): "))
                print("\n📌", api.registrar_chamada(origem, destino, duracao))
            
            elif opcao == "8":
                print("\n=== GERAR FATURA ===")
                cpf = input("CPF do cliente: ")
                print("\n", api.gerar_fatura(cpf))
            
            elif opcao == "9":
                print("\n=== LISTAR FATURAS ===")
                cpf = input("CPF do cliente: ")
                faturas = api.listar_faturas(cpf)
                print()
                for fatura in faturas:
                    print(fatura)
            
            elif opcao == "10":
                print("\n=== ESTATÍSTICAS DO SISTEMA ===")
                print("\n", api.obter_estatisticas())
            
            elif opcao == "0":
                print("\n👋 Encerrando cliente Python...")
                break
            
            else:
                print("❌ Opção inválida!")
        
        except KeyboardInterrupt:
            print("\n\n👋 Encerrando...")
            break
        except Exception as e:
            print(f"❌ Erro: {e}")


if __name__ == "__main__":
    main()
