# Territory Predictive Insights (CitizenTech)

**Equipe:** (nome e RM de cada integrante)
**Repositório GitHub:** (link)

## Recorte escolhido
Clima local: chuva intensa e risco de alagamento em pontos vulneráveis.

## Público beneficiado e decisão apoiada
(Escrevam: quem sofre o problema? Ex.: moradores de áreas baixas perto do córrego.
Qual decisão o sistema apoia? Ex.: quando avisar moradores / acionar a defesa civil.)

## Como compilar e rodar
Requer Java 11+.
```
javac -encoding UTF-8 *.java
java -Dfile.encoding=UTF-8 SistemaMonitoramento
```
Dica: use primeiro a opção 4 (simular leituras) e depois a 2 (previsões e alertas).
Código de acesso inicial para alterar limiares: `1234`.
Bônus implementado: o histórico é salvo em `historico.csv` ao sair e recarregado ao iniciar.

## Estrutura e conceitos de OOP
- `ComponenteTerritorial` (abstrata) -> `EstacaoDeColeta` (extends)
- `Sensor` (interface) -> `SensorChuva`, `SensorNivelRio`, `SensorUmidade` (implements)
- `DadosTerritorio`: atributos privados, setters com validação, limiares protegidos por código
- `EstrategiaPrevisao` (abstrata) -> `PrevisaoMediaMovel`, `PrevisaoComparacao` (uso de super)
- `NivelAlerta` (enum): NORMAL, ATENÇÃO, ALERTA, CRÍTICO
- `SistemaMonitoramento`: menu, previsões e alertas

## Decisões da equipe
(Escrevam: por que esses limiares? Por que janela 5 e período 3? Por que simulação com tendência?)

## Reflexão crítica (meia página — escrevam com as palavras de vocês)
1. Quando o sistema deve se calar? (o que o código faz hoje com poucas leituras e por quê)
2. O que os dados não representam? (simulação, 1 estação para a região inteira...)
3. Por que o alerta apoia a decisão da pessoa em vez de decidir por ela?

## Uso de IA
(Declarem: onde usaram IA, o que ela gerou e como vocês revisaram e testaram o código.)

## Divisão de tarefas
(Quem fez sensores / previsões / dados do território e menu. Todos devem ter commits.)

## O que faltou / como continuaria
(Se houver.)
