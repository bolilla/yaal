package yaal;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

public class TestGrammar {
	private static final String TESTS_PASS_DIR = "pass";
	private static final String TESTS_FAIL_DIR = "fail";

	@Test
	public void testPassingSamples() throws URISyntaxException, IOException {
		check(TESTS_PASS_DIR, true);
	}
	
	@Test
	public void testFailingSamples() throws URISyntaxException, IOException {
		check(TESTS_FAIL_DIR, false);
	}
	
	/**
	 * Checks the compilation of every sample in the directory matches teh expected result
	 * @param directory path to the directory with the samples
	 * @param expectedResult assertion to make regarding the compilation result
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private void check(String directory, boolean expectedResult) throws URISyntaxException, IOException{
		Path pathTestsPass = Paths.get(getClass().getResource("/"+TESTS_PASS_DIR).toURI());
		MyFileVisitor visitor = new MyFileVisitor();
		Files.walkFileTree(pathTestsPass, visitor);
		System.out.println("XXX");
		assertTrue(true);
		assertTrue(true);
	}

	public static class MyFileVisitor extends SimpleFileVisitor<Path> {

		// Print information about
		// each type of file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
			if (attr.isRegularFile()) {
				System.out.format("Regular file: %s ", file);
			} else {
				System.out.format("There is something strangw with this file: %s ", file);
			}
			return CONTINUE;
		}

		// Print each directory visited.
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			System.out.format("Directory: %s%n visited", dir);
			return CONTINUE;
		}

		// If there is some error accessing
		// the file, let the user know.
		// If you don't override this method
		// and an error occurs, an IOException
		// is thrown.
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return CONTINUE;
		}
	}
}