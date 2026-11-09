# MVP – Aplicação de consulta cliente-servidor (Projeto Integrador III-A)

Protótipo mínimo em Java (sem bibliotecas externas, sem banco de dados, sem interface gráfica).
Os dados de exemplo são **fictícios** (ONG "Mãos Solidárias") e devem ser substituídos
pelos dados da instituição escolhida pela equipe.

## Requisitos
- JDK 17 ou superior (testado com JDK 21)

## Estrutura
```
mvp/
├── dados/servicos.csv                    # informações da instituição (id;categoria;nome;horario;descricao)
└── src/
    ├── modelo/Servico.java               # um item de informação (record)
    ├── servidor/RepositorioServicos.java # carrega o CSV e faz as buscas
    ├── servidor/ProcessadorComandos.java # protocolo: comando recebido -> resposta
    ├── servidor/Servidor.java            # ServerSocket + uma thread por cliente
    └── cliente/Cliente.java              # CLI: envia comandos e exibe respostas
```

## Compilar
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```
(Windows PowerShell: `javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`)

## Executar
Terminal 1 – servidor (porta 5000 e arquivo `dados/servicos.csv` por padrão):
```bash
java -cp out servidor.Servidor
```
Terminal 2 – cliente:
```bash
java -cp out cliente.Cliente localhost 5000
```

### Acentos no console
Se aparecerem `?` no lugar de acentos (comum no Windows), execute com a codificação explícita:
```bash
java -Dstdout.encoding=UTF-8 -cp out servidor.Servidor
java -Dstdout.encoding=UTF-8 -cp out cliente.Cliente localhost 5000
```
No `cmd` do Windows, rode antes `chcp 65001`. A comunicação em rede já usa UTF-8; só a exibição no console depende disso.

## Protocolo (texto, uma linha por mensagem)
| Comando          | Resposta                                   |
|------------------|--------------------------------------------|
| `LISTAR`         | todos os itens (id \| categoria \| nome)   |
| `BUSCAR <termo>` | itens que contêm o termo                   |
| `DETALHAR <id>`  | todos os dados de um item                  |
| `AJUDA`          | lista de comandos                          |
| `SAIR`           | encerra a conexão                          |

Toda resposta começa com `OK` ou `ERRO <motivo>` e termina com uma linha `FIM`.

## Limitações conhecidas (para evoluir nas próximas etapas)
- A comunicação é em **texto puro**: quem estiver na rede consegue ler o tráfego (será tratado na Aula 3).
- Não há limite de tamanho de linha, de conexões simultâneas nem registro (log) em arquivo.
- Não há autenticação: qualquer cliente que alcance a porta consegue consultar.
