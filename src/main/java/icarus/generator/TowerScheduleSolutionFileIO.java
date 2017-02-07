package icarus.generator;

import java.io.File;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import icarus.model.TowerSchedule;

/**
 * Implementation of an Optaplanner SolutionFileIO.  Reads and writes a TowerSchedule file
 * for benchmarking purposes
 */
public class TowerScheduleSolutionFileIO implements SolutionFileIO
{

   /**
    * Returns the extension of the input file
    * @see org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO#getInputFileExtension()
    * @return The extension of the input file
    */
   @Override
   public String getInputFileExtension()
   {
      return ".txt";
   }

   /**
    * Returns the extension of the output file
    * @see org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO#getOutputFileExtension()
    * @return The extension of the output file
    */
   @Override
   public String getOutputFileExtension()
   {
      return ".txt";
   }

   /**
    * Reads a file into a TowerSchedule for optaplanner benchmarking
    * @see org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO#read(java.io.File)
    * @param file The file to read
    * @return The TowerSchedule file
    */
   @Override
   public Solution<HardSoftLongScore> read(File file)
   {
      return TowerScheduleGenerator.createFromImport(file.getName());
   }

   /**
    * Writes a towerschedule file for optaplanner benchmarking
    * @see org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO#write(org.optaplanner.core.api.domain.solution.Solution, java.io.File)
    * @param schedule The tower schedule file to write
    * @param file The file to write to
    */
   @Override
   public void write(@SuppressWarnings("rawtypes") Solution schedule, File file)
   {
      ((TowerSchedule)schedule).saveConfigToFile(file.getName());
   }

}