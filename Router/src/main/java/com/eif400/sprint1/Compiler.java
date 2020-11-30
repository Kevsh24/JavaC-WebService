/*
Universidad Nacional
Paradigmas de programación

Dr. Carlos Loría Sáenz

Estudiante:
Kevin Salas Hernández  - 116680114

Last edit: Oct,2020
Comments: basado en ejemplo dado por el profesor, se realizan configuraciones en javac y se omiten warnigns.
*/

package com.eif400.sprint1;

import javax.tools.*;
import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.Arrays;

import java.util.Locale;
import java.nio.charset.Charset;

public class Compiler {
   public static void main(String[] args) throws Exception {

      if (args.length == 0){
          System.err.println("*** Please provide an existing .java filename ***");
          System.exit(-1);
      }

      String javafilename = args[0];
      var file = new File( javafilename );
      
      // Get compiler and configure compilation task
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      DiagnosticCollector< JavaFileObject > diagsCollector = new DiagnosticCollector<>();
      Locale locale = null;
      Charset charset = null;
      String outdir = "target/classes/com/eif400/sprint1/dsrc";
      String optionsString = String.format("-nowarn --enable-preview --release 14 -cp target/classes/com/eif400/sprint1/dsrc -d %s", outdir);
         
      try {
         var fileManager = compiler.getStandardFileManager( diagsCollector, locale, charset );
         var sources = fileManager.getJavaFileObjectsFromFiles(Arrays.asList( file ) );
         
         Writer writer = new PrintWriter(System.err);
         // Also check out compiler.isSupportedOption() if needed
         
         Iterable<String> options = Arrays.asList(optionsString.split(" "));
         Iterable<String> annotations = null;
         var compileTask = compiler.getTask( writer, 
                                      fileManager, 
                                      diagsCollector, 
                                      options, 
                                      annotations, 
                                      sources );
         compileTask.call();
      } catch(Exception e){
          System.err.format("%s%n", e);
          System.exit(-1);
      }
      // Report diagnostics
      if (diagsCollector.getDiagnostics().size() == 0){
          System.out.format( "No errors found in your code.%n");
          System.out.format( "*** Your code is ready to work! ***%n");
          System.exit(0);
      }
      for( var d: diagsCollector.getDiagnostics() ) {
         long pos = d.getLineNumber();
         if(pos >= 0){
            String location = String.format("Line: %d", pos);
            System.err.format("%s %s %n",location, d.getMessage( locale ));
         } 
      } 
   }
}