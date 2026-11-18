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

Os limites podem ser alterados pelo menu (opção 3 → 4), informando o código de acesso. A classe `DadosTerritorio` só aceita a alteração se o código estiver correto, se a ordem for atenção < alerta < crítico e se os valores estiverem dentro da faixa física do sensor (chuva 0–150 mm/h, rio 0–10 m, umidade 0–100%). Caso contrário, a alteração é rejeitada e os valores anteriores são mantidos.

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

### 6.1 Sobre a quantidade mínima de leituras

**Por que é importante ter uma quantidade mínima de dados antes de gerar uma previsão?**
Com poucas leituras, uma única variação fora do comum pesa demais no resultado. Na média móvel, se houver só duas leituras e uma delas for um pico momentâneo, a "média" passa a representar esse pico, e não o comportamento real. Por isso definimos 5 leituras para a Média Móvel e 6 para a Comparação de Períodos (dois períodos completos de 3).

**O que poderia acontecer se o sistema emitisse alertas com poucas leituras?**
O sistema geraria falsos alarmes ou, ao contrário, deixaria passar situações reais. A Comparação de Períodos é a mais sensível: ela soma a diferença entre os períodos à média atual, então, com períodos de uma leitura só, um salto de 10 mm/h seria projetado como mais 10 mm/h na próxima leitura. Alarmes falsos frequentes fazem os moradores pararem de confiar nos avisos, e isso é perigoso quando o risco for real.

**Como a ausência de dados suficientes pode afetar a confiabilidade de uma previsão?**
Sem dados suficientes não dá para separar tendência de ruído. Por isso, nessa situação o sistema prefere "se calar": informa quantas leituras existem, qual é o mínimo e não gera alerta. Achamos melhor ser transparente sobre a falta de dados do que mostrar um número que parece preciso mas não é.

### 6.2 Sobre as limitações do projeto

**Quais são as limitações de utilizar `Math.random()` para simular condições ambientais?**
Os valores seguem uma regra que nós mesmas escolhemos (um passeio aleatório com leve tendência de alta), e não a física real. Os três sensores são independentes, enquanto na realidade a chuva forte faz o rio subir algum tempo depois e aumenta a umidade. A simulação também não tem sazonalidade (época de chuvas), horário do dia nem eventos extremos repentinos. Ela serve para testar o funcionamento do código, mas não para validar se as previsões acertam.

**Quais problemas podem surgir ao utilizar apenas uma estação de coleta para representar toda a comunidade?**
Uma estação mede um ponto só. Pode estar chovendo forte na cabeceira do córrego e fraco onde a estação está, e o alerta chegaria tarde. Além disso, se essa estação falhar, a comunidade fica sem nenhum monitoramento, porque não há redundância. Também não dá para saber quais ruas ou áreas baixas estão em maior risco.

**Como a instalação de sensores físicos em diferentes pontos poderia melhorar o monitoramento?**
Sensores ao longo do córrego (montante, meio e jusante) permitiriam ver a água subindo antes de chegar às áreas mais baixas, ganhando tempo para avisar os moradores. Com vários pontos, uma leitura estranha em um sensor poderia ser comparada com os vizinhos para identificar defeito, e a falha de uma estação não deixaria a comunidade descoberta. Nosso código já permite isso, porque `EstacaoDeColeta` é uma classe que pode ter várias instâncias.

### 6.3 Sobre a utilização das previsões

**Por que uma previsão não deve ser considerada uma garantia de que um alagamento acontecerá?**
As estratégias apenas projetam o comportamento recente das leituras. A média móvel diz qual é o "nível recente esperado" e a comparação supõe que a tendência vai continuar igual, mas a chuva pode parar ou piorar de repente. Além disso, o alagamento depende de fatores que o sistema não mede, como bueiros entupidos, lixo no córrego e impermeabilização do solo.

**Quais outras informações deveriam ser consideradas antes de tomar uma decisão?**
Previsão meteorológica oficial (radar, alertas do CGE e da Defesa Civil), histórico de alagamentos da região, relatos de moradores e agentes em campo, situação da drenagem e o horário (um alerta de madrugada exige outra forma de aviso). Por isso o próprio alerta do sistema pede para confirmar em campo antes de agir.

**Quais riscos existem quando as pessoas dependem exclusivamente dos alertas emitidos pelo sistema?**
Se o sistema falhar (sensor quebrado, falta de energia, limiar mal configurado), as pessoas podem achar que está tudo normal só porque nenhum alerta apareceu. O silêncio do sistema não significa segurança. Também existe o risco contrário: muitos alertas falsos levam as pessoas a ignorar os avisos. O sistema deve ser uma ferramenta de apoio somada ao julgamento humano e aos canais oficiais, nunca a única fonte de decisão.

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
