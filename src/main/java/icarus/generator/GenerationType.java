package icarus.generator;

/**
 * Determines whether or not phone and tower generation should be done with
 * fixed, defined values, or random ones over a range
 */
public enum GenerationType
{
   /** Used to indicate a variable should use a fixed value, defined in a properties file */
   FIXED,
   /**
    * Used to indicate the application should generate a random value,
    * bounds are defined in a properties file
    */
   RANDOM
}