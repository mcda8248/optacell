<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>

  <parallelBenchmarkCount>AUTO</parallelBenchmarkCount>
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
  </inheritedSolverBenchmark>


<#list [11] as entityTabuSize>
<#list [1000] as acceptedCountLimit>
  <solverBenchmark>
    <name>entityTabuSize ${entityTabuSize} countLimit ${acceptedCountLimit}</name>
    <solver>
    <localSearch>
    <changeMoveSelector/>

    <acceptor>
      <entityTabuSize>${entityTabuSize}</entityTabuSize>
      <!-- 
        Things to try and benchmark:
        <entityTabuRatio>entityTabuRatio</entityTabuRatio>
        <valueTabuRatio>valueTabuRatio</valueTabuRatio>
        -->
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
        <!--
        To try
        <finalistPodiumType>STRATEGIC_OSCILLATION</finalistPodiumType>
        -->
    </forager>
     <termination>
      <unimprovedSecondsSpentLimit>15</unimprovedSecondsSpentLimit>
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
          <!--
              Things to try and benchmark:
        <valueTabuSize>valueTabuSize</valueTabuSize>
        <entityTabuSize>entityTabuSize</entityTabuSize>
        <entityTabuRatio>entityTabuRatio</entityTabuRatio>
        <valueTabuRatio>valueTabuRatio</valueTabuRatio>
        -->
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
    </forager>
     <termination>
      <unimprovedSecondsSpentLimit>15</unimprovedSecondsSpentLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>
</#list>

<#list [200] as lateAcceptanceSize>
<#list [12] as acceptedCountLimit>
  <solverBenchmark>
    <name>lateAcceptance ${lateAcceptanceSize} countLimit ${acceptedCountLimit}</name>
    <solver>
  <localSearch>
    <changeMoveSelector/>
    <acceptor>
      <lateAcceptanceSize>${lateAcceptanceSize}</lateAcceptanceSize>
        <!--
         To try
         <valueTabuSize></valueTabuSize>
         <entityTabuSize></entityTabuSize>
         -->
    </acceptor>
    <forager>
      <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
    </forager>
     <termination>
      <unimprovedSecondsSpentLimit>15</unimprovedSecondsSpentLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>

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
      <unimprovedSecondsSpentLimit>15</unimprovedSecondsSpentLimit>
    </termination>
  </localSearch>
  </solver>
  </solverBenchmark>
</#list>
</#list>
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