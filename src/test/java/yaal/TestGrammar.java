package yaal;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;

import yaal.yaalParser.PolicyContext;

public class TestGrammar {
	private static final String TESTS_PASS_DIR = "pass";
	private static final String TESTS_FAIL_DIR = "fail";

	@Test
	public void testPassingSamples() throws URISyntaxException, IOException {
		assertTrue(check(TESTS_PASS_DIR, true));
	}

	@Test
	public void testFailingSamples() throws URISyntaxException, IOException {
		assertTrue(check(TESTS_FAIL_DIR, false));
	}

	/**
	 * Checks the compilation of every sample in the directory matches teh
	 * expected result
	 * 
	 * @param directory
	 *            path to the directory with the samples
	 * @param expectedResult
	 *            assertion to make regarding the compilation result
	 * @throws URISyntaxException
	 * @throws IOException
	 *             returns true iff there has been no error
	 */
	private boolean check(String directory, boolean expectedResult) throws URISyntaxException, IOException {
		Path pathTests = Paths.get(getClass().getResource("/" + directory).toURI());
		MyFileVisitor visitor = new MyFileVisitor(expectedResult);
		Files.walkFileTree(pathTests, visitor);
		return !visitor.anyError;
	}

	/**
	 * Visits the files in a directory, applies YAAL grammar and checks whether
	 * the contents of the file matches the grammar. If any error is found, the
	 * variable anyError is updated to true
	 */
	public static class MyFileVisitor extends SimpleFileVisitor<Path> {
		private boolean expectedResult;
		private boolean anyError = false;

		/**
		 * Create a visitor stating the expected result of applying the grammar
		 * to the files it visits
		 */
		public MyFileVisitor(boolean expectedResult) {
			this.expectedResult = expectedResult;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
			if (attr.isRegularFile()) {
				String readableExpectation = expectedResult?"PASS":"FAIL";
				System.out.format("Test: \"%s\" (expected result: %s)%n", file, readableExpectation);
				if (checkGrammarOfFile(file) != expectedResult) {
					anyError = true;
				}
			} else {
				System.out.format("There is something strange with this file: %s%n", file);
				fail("Found unexpected file: " + file);
			}
			return CONTINUE;
		}

		/**
		 * Checks whether file in given path matches YAAL grammar
		 * 
		 * @param file
		 *            file to check
		 * @return true iff the contents of the file match the grammar
		 */
		private boolean checkGrammarOfFile(Path file) {
			String expression;
			try {
				expression = new String(Files.readAllBytes(file), "UTF-8");
				ANTLRInputStream input = new ANTLRInputStream(expression);
				TokenStream tokens = new CommonTokenStream(new yaalLexer(input));
				yaalParser parser = new yaalParser(tokens);
				PolicyContext ret = parser.policy();
				return ret.exception == null;
			} catch (Exception e) {
				return false;
			}
		}

		// Print each directory to visit.
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			System.out.format("Directory: \"%s\" to be visited%n", dir);
			return CONTINUE;
		}

		// Print each visited directory.
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			System.out.format("Directory: \"%s\" visited%n", dir);
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.out.format("Failed to visit this file: %s%n", file);
			fail();
			return CONTINUE;
		}
	}
}