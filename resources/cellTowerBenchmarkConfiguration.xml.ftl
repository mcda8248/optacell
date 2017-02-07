<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>

  <benchmarkDirectory>benchmarking</benchmarkDirectory>
  <warmUpSecondsSpentLimit>30</warmUpSecondsSpentLimit>
  <benchmarkReport>
    <solverRankingType>TOTAL_SCORE</solverRankingType>
  </benchmarkReport>

  <inheritedSolverBenchmark>
     <problemBenchmarks>
       <solutionFileIOClass>icarus.generator.TowerScheduleSolutionFileIO</solutionFileIOClass>
       <inputSolutionFile>problemSpace.txt</inputSolutionFile>
     </problemBenchmarks>
     <solver>
       <scanAnnotatedClasses>
         <packageInclude>icarus.model</packageInclude>
       </scanAnnotatedClasses>
       <scoreDirectorFactory>
         <scoreDefinitionType>HARD_SOFT_LONG</scoreDefinitionType>
         <incrementalScoreCalculatorClass>icarus.scoring.CellTowerIncrementalScoreCalculator</incrementalScoreCalculatorClass>
         <initializingScoreTrend>ONLY_UP</initializingScoreTrend>
       </scoreDirectorFactory>
     </solver>
     <subSingleCount>1</subSingleCount>
  </inheritedSolverBenchmark>

<!-- 
<#list [11] as entityTabuSize>
<#list [1000] as acceptedCountLimit>
  <solverBenchmark>
    <name>entityTabuSize ${entityTabuSize} countLimit ${acceptedCountLimit}</name>
    <solver>
    <localSearch>
    <changeMoveSelector/>

    <acceptor>
      <entityTabuSize>${entityTabuSize}</entityTabuSize>
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
    </forager>
     <termination>
      <unimprovedStepCountLimit>100</unimprovedStepCountLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>

<#list [2] as hardTemperature>
<#list [100] as softTemperature>
<#list [10] as acceptedCountLimit>
  <solverBenchmark>
    <name>hardTemp ${hardTemperature} softTemp ${softTemperature} countLimit ${acceptedCountLimit}</name>
    <solver>
    <localSearch>
    <changeMoveSelector/>

    <acceptor>
      <simulatedAnnealingStartingTemperature>${hardTemperature}hard/${softTemperature}soft</simulatedAnnealingStartingTemperature>
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
    </forager>
     <termination>
      <unimprovedStepCountLimit>100</unimprovedStepCountLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>
</#list>
-->

<#list [200] as lateAcceptanceSize>
<#list [12] as acceptedCountLimit>
  <solverBenchmark>
    <name>lateAcceptance ${lateAcceptanceSize} countLimit ${acceptedCountLimit}</name>
    <solver>
  <localSearch>
    <changeMoveSelector/>
    <acceptor>
      <lateAcceptanceSize>${lateAcceptanceSize}</lateAcceptanceSize>
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
    </forager>
     <termination>
      <unimprovedStepCountLimit>200</unimprovedStepCountLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>

<!--
<#list [200] as stepCountingSize>
<#list [12] as acceptedCountLimit>
  <solverBenchmark>
    <name>stepCountingSize ${stepCountingSize} countLimit ${acceptedCountLimit}</name>
    <solver>
  <localSearch>
    <changeMoveSelector/>
    <acceptor>
      <stepCountingHillClimbingSize>${stepCountingSize}</stepCountingHillClimbingSize>
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
    </forager>
     <termination>
      <unimprovedStepCountLimit>300</unimprovedStepCountLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>
-->
  <!-- 
    Construction Heuristics tests, caching values had best performance
  <solverBenchmark>
    <name>ValuePhased</name>
    <solver>
    <constructionHeuristic>
    <pooledEntityPlacer>
      <changeMoveSelector>
        <valueSelector>
         <cacheType>PHASE</cacheType>
        </valueSelector>
      </changeMoveSelector>
    </pooledEntityPlacer>
  </constructionHeuristic>
  </solver>
  </solverBenchmark>
  
  <solverBenchmark>
    <name>ValuePhasedAndShuffled</name>
    <solver>
    <constructionHeuristic>
    <pooledEntityPlacer>
      <changeMoveSelector>
        <valueSelector>
         <cacheType>PHASE</cacheType>
         <selectionOrder>SHUFFLED</selectionOrder>
        </valueSelector>
      </changeMoveSelector>
    </pooledEntityPlacer>
  </constructionHeuristic>
  </solver>
  </solverBenchmark>
-->
</plannerBenchmark>