# Territory Predictive Insights (CitizenTech)

**Equipe:** Ana Clara Alcântara - RM 568463, Ana Flavia Chaves - RM 566603 e Izabely Santos - RM 567088

**Repositório GitHub:** [https://github.com/flavsna/gs-oop-territory-predictive-insights](https://github.com/flavsna/gs-oop-territory-predictive-insights)

## 1. Recorte escolhido

Nosso projeto aborda o risco de alagamentos em regiões vulneráveis, utilizando o monitoramento de condições climáticas e do nível de um rio para gerar previsões e alertas.

Para representar esse cenário, criamos o território fictício **Comunidade Córrego Norte**, localizado na Zona Leste de São Paulo, com uma população estimada de 12 mil habitantes.

O sistema utiliza três sensores simulados: chuva, nível do rio e umidade. As leituras são geradas por meio de `Math.random()`, com pequenas variações e uma tendência de aumento, representando possíveis mudanças nas condições ambientais.

Os dados são utilizados por duas estratégias de previsão, que analisam os valores registrados e ajudam a identificar situações que podem representar riscos para a comunidade.

## 2. Público beneficiado e decisão apoiada

O principal público beneficiado são os moradores de regiões próximas a córregos e áreas sujeitas a alagamentos, especialmente aqueles que vivem em locais mais vulneráveis.

O sistema busca apoiar a identificação antecipada de situações de risco, auxiliando na decisão de quando emitir alertas e considerar medidas preventivas, como avisar os moradores e acionar a Defesa Civil.

As previsões não substituem a avaliação humana, mas oferecem informações que podem contribuir para uma tomada de decisão mais rápida.

## 3. Como compilar e executar

É necessário ter o Java JDK instalado e acessar a pasta que contém os arquivos `.java`.

**No terminal:**

```bash
javac -encoding UTF-8 *.java
java "-Dfile.encoding=UTF-8" SistemaMonitoramento
```

**No PowerShell do Windows:**

```powershell
javac -encoding UTF-8 (Get-ChildItem *.java).Name
java "-Dfile.encoding=UTF-8" SistemaMonitoramento
```

Após iniciar o programa, o usuário pode utilizar as opções disponíveis no menu para consultar o estado dos sensores, simular novas leituras, executar previsões e gerenciar os dados do território.

Para obter previsões, é necessário simular leituras suficientes para atender ao mínimo exigido por cada estratégia.

O código de acesso inicial para alterar os dados do território é `1234`.

O histórico das leituras é salvo no arquivo `historico.csv` ao encerrar o programa e carregado novamente na próxima execução.

## 4. Estrutura do projeto e conceitos de orientação a objetos

O projeto foi desenvolvido em Java utilizando conceitos de orientação a objetos.

- **ComponenteTerritorial:** classe abstrata que representa um componente do território e reúne características comuns, como identificação, nome, região e estado de funcionamento.
- **Sensor:** interface que define os comportamentos necessários para os sensores, como realizar leituras, verificar seu funcionamento e informar seu tipo.
- **EstacaoDeColeta:** representa a estação responsável por reunir os sensores utilizados no monitoramento.
- **SensorChuva:** realiza leituras simuladas de chuva, em milímetros por hora (mm/h).
- **SensorNivelRio:** realiza leituras simuladas do nível do rio, em metros (m).
- **SensorUmidade:** realiza leituras simuladas de umidade, em porcentagem (%).
- **DadosTerritorio:** mantém os dados e limites de alerta do território, utilizando encapsulamento e validações para controlar alterações.
- **EstrategiaPrevisao:** classe abstrata que define a estrutura das estratégias de previsão.
- **PrevisaoMediaMovel:** implementa a previsão por média móvel, utilizando as últimas cinco leituras. É necessário ter pelo menos cinco registros.
- **PrevisaoComparacao:** compara dois períodos de três leituras. É necessário ter pelo menos seis registros. A previsão considera a média atual somada à diferença entre a média atual e a média anterior.
- **NivelAlerta:** representa os níveis de alerta utilizados pelo sistema.
- **SistemaMonitoramento:** integra os componentes e disponibiliza o menu de interação com o usuário.

### Limites de alerta

Os limites são definidos em três níveis: atenção, alerta e crítico.

| Sensor           | Atenção | Alerta | Crítico |
| ---------------- | ------: | -----: | ------: |
| Chuva (mm/h)     |      20 |     40 |      60 |
| Nível do rio (m) |     2,5 |    3,5 |     4,5 |
| Umidade (%)      |      70 |     85 |      95 |

Os limites podem ser alterados por meio do código de acesso, na classe `DadosTerritorio`.

### Funcionamento das previsões e dos alertas

O sistema utiliza duas estratégias para analisar os dados coletados.

A **Média Móvel** calcula a média das últimas cinco leituras, enquanto a **Comparação de Períodos** compara a média das três leituras mais recentes com a média das três anteriores.

Quando uma estratégia identifica que a previsão ultrapassa algum limite definido, o sistema apresenta um alerta com o sensor correspondente, o valor previsto, a estratégia utilizada, o limite ultrapassado e a justificativa do alerta.

Caso ainda não exista a quantidade mínima de leituras, o sistema informa que são necessários mais dados para realizar a previsão. Nesse caso, não é gerado um alerta.

Os alertas servem como apoio à decisão e não representam uma confirmação de que um alagamento acontecerá.

## 5. Decisões da equipe

Durante o desenvolvimento, definimos limites de atenção, alerta e nível crítico para cada sensor, permitindo que o sistema classifique diferentes situações de risco.

Também escolhemos utilizar duas estratégias de previsão para analisar os dados de maneiras diferentes. A média móvel permite observar o comportamento recente das leituras, enquanto a comparação de períodos ajuda a identificar tendências de aumento ou diminuição.

A utilização de dados simulados permitiu desenvolver e testar o funcionamento do sistema sem depender de sensores físicos. Além disso, definimos quantidades mínimas de leituras para evitar previsões baseadas em poucos dados.

## 6. Reflexão crítica

**1. Sobre a quantidade mínima de leituras:**

- Por que é importante ter uma quantidade mínima de dados antes de gerar uma previsão?
- O que poderia acontecer se o sistema emitisse alertas com poucas leituras?
- Como a ausência de dados suficientes pode afetar a confiabilidade de uma previsão?

**2. Sobre as limitações do projeto:**

- Quais são as limitações de utilizar `Math.random()` para simular condições ambientais?
- Quais problemas podem surgir ao utilizar apenas uma estação de coleta para representar toda a comunidade?
- Como a instalação de sensores físicos em diferentes pontos poderia melhorar o monitoramento?

**3. Sobre a utilização das previsões:**

- Por que uma previsão não deve ser considerada uma garantia de que um alagamento acontecerá?
- Quais outras informações deveriam ser consideradas antes de tomar uma decisão?
- Quais riscos existem quando as pessoas dependem exclusivamente dos alertas emitidos pelo sistema?

## 7. Uso de inteligência artificial

Durante o desenvolvimento, utilizamos o Claude como apoio para gerar uma primeira versão da estrutura do código.

Após isso, a equipe analisou a organização do projeto, estudou o funcionamento das classes e realizou testes para verificar a execução e o comportamento da aplicação.

Também utilizamos o ChatGPT como apoio na estruturação deste README.

## 8. Divisão de tarefas

O desenvolvimento foi organizado em três etapas, com responsabilidades distribuídas entre as integrantes.

| Integrante              | Responsabilidades                                                                                   | Arquivos                                                                                                                              |
| ----------------------- | --------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **Ana Flavia Chaves**   | Desenvolvimento da classe abstrata, interface, estação de coleta e sensores.                        | `ComponenteTerritorial.java`, `EstacaoDeColeta.java`, `Sensor.java`, `SensorChuva.java`, `SensorNivelRio.java` e `SensorUmidade.java` |
| **Ana Clara Alcântara** | Desenvolvimento das estratégias de previsão e dos níveis de alerta.                                 | `EstrategiaPrevisao.java`, `PrevisaoMediaMovel.java`, `PrevisaoComparacao.java` e `NivelAlerta.java`                                  |
| **Izabely Santos**      | Desenvolvimento dos dados do território, integração do sistema, menu e finalização da documentação. | `DadosTerritorio.java`, `SistemaMonitoramento.java` e `README.md`                                                                     |

A criação do repositório e a estrutura inicial do README foram realizadas antes do desenvolvimento das classes. A implementação foi organizada por etapas, começando pelos sensores, seguindo para as estratégias de previsão e, por fim, a integração do sistema.

## 9. O que faltou e como continuaríamos

O projeto utiliza dados simulados, portanto ainda não realiza o monitoramento de condições ambientais reais. Além disso, o território é representado por apenas uma estação de coleta, o que limita a identificação de diferenças entre regiões da comunidade.

Outra possibilidade de melhoria seria implementar a persistência dos limites configurados, permitindo que as alterações realizadas pelo usuário continuem salvas após o encerramento do programa.

Também poderíamos ampliar a simulação de falhas dos sensores e desenvolver estratégias de previsão mais completas, utilizando dados históricos reais.

Como continuidade, pretendemos tornar o sistema mais próximo de uma aplicação real de monitoramento, com sensores físicos distribuídos pelo território e informações mais precisas para apoiar a prevenção de alagamentos.
