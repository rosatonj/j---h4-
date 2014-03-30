// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Hashtable;

import static jminusminus.TokenKind.*;

/**
 * A lexical analyzer for j--, that has no backtracking mechanism.
 * 
 * When you add a new token to the scanner, you must also add an entry in the
 * TokenKind enum in TokenInfo.java specifying the kind and image of the new
 * token.
 */

class Scanner {

	/** End of file character. */
	public final static char EOFCH = CharReader.EOFCH;

	/** Keywords in j--. */
	private Hashtable<String, TokenKind> reserved;

	/** Source characters. */
	private CharReader input;

	/** Next unscanned character. */
	private char ch;

	/** Whether a scanner error has been found. */
	private boolean isInError;

	/** Source file name. */
	private String fileName;

	/** Line number of current token. */
	private int line;

	/**
	 * Construct a Scanner object.
	 * 
	 * @param fileName
	 *            the name of the file containing the source.
	 * @exception FileNotFoundException
	 *                when the named file cannot be found.
	 */

	public Scanner(String fileName) throws FileNotFoundException {
		this.input = new CharReader(fileName);
		this.fileName = fileName;
		isInError = false;

		// Keywords in j--
		reserved = new Hashtable<String, TokenKind>();
		reserved.put(ABSTRACT.image(), ABSTRACT);
		reserved.put(ASSERT.image(), ASSERT); // ADDED
		reserved.put(BOOLEAN.image(), BOOLEAN);
		reserved.put(BREAK.image(), BREAK); // Added
		reserved.put(BYTE.image(), BYTE); // Added
		reserved.put(CASE.image(), CASE); // Added
		reserved.put(CATCH.image(), CATCH); // Added
		reserved.put(CHAR.image(), CHAR);
		reserved.put(CLASS.image(), CLASS);
		reserved.put(CONST.image(), CONST); // Added
		reserved.put(CONTINUE.image(), CONTINUE); // Added
		reserved.put(DEFAULT.image(), DEFAULT); // Added
		reserved.put(DO.image(), DO); // Added
		reserved.put(DOUBLE.image(), DOUBLE); // Added
		reserved.put(ELSE.image(), ELSE);
		reserved.put(ENUM.image(), ENUM); // Added
		reserved.put(EXTENDS.image(), EXTENDS);
		reserved.put(FALSE.image(), FALSE);
		reserved.put(FINAL.image(), FINAL); // Added
		reserved.put(FINALLY.image(), FINALLY); // Added
		reserved.put(FLOAT.image(), FLOAT); // Added
		reserved.put(FOR.image(), FOR); // Added
		reserved.put(GOTO.image(), GOTO); // Added
		reserved.put(IF.image(), IF);
		reserved.put(IMPLEMENTS.image(), IMPLEMENTS); // Added
		reserved.put(IMPORT.image(), IMPORT);
		reserved.put(INSTANCEOF.image(), INSTANCEOF);
		reserved.put(INT.image(), INT);
		reserved.put(INTERFACE.image(), INTERFACE); // Added
		reserved.put(LONG.image(), LONG); // Added
		reserved.put(NATIVE.image(), NATIVE); // Added
		reserved.put(NEW.image(), NEW);
		reserved.put(NULL.image(), NULL);
		reserved.put(PACKAGE.image(), PACKAGE);
		reserved.put(PRIVATE.image(), PRIVATE);
		reserved.put(PROTECTED.image(), PROTECTED);
		reserved.put(PUBLIC.image(), PUBLIC);
		reserved.put(RETURN.image(), RETURN);
		reserved.put(SHORT.image(), SHORT); // Added
		reserved.put(STATIC.image(), STATIC);
		reserved.put(STRICTFP.image(), STRICTFP); // Added
		reserved.put(SUPER.image(), SUPER);
		reserved.put(SWITCH.image(), SWITCH); // Added
		reserved.put(SYNCHRONIZED.image(), SYNCHRONIZED); // Added
		reserved.put(THIS.image(), THIS);
		reserved.put(THROW.image(), THROW); // Added
		reserved.put(THROWS.image(), THROWS); // Added
		reserved.put(TRANSIENT.image(), TRANSIENT); // Added
		reserved.put(TRY.image(), TRY); // Added
		reserved.put(TRUE.image(), TRUE);
		reserved.put(VOID.image(), VOID);
		reserved.put(VOLATILE.image(), VOLATILE); // Added
		reserved.put(WHILE.image(), WHILE);

		// Prime the pump.
		nextCh();
	}

	/**
	 * Scan the next token from input.
	 * 
	 * @return the the next scanned token.
	 */

	public TokenInfo getNextToken() {
		StringBuffer buffer;
		boolean moreWhiteSpace = true;
		while (moreWhiteSpace) {
			while (isWhitespace(ch)) {
				nextCh();
			}
			if (ch == '/') {
				nextCh();
				// Multi-line comments
				if (ch == '*') {
					nextCh();
					while (ch != EOFCH) { // continue to loop till we get to the
											// end of file
						nextCh();
						while (ch == '*') { // "*" can still be in a comment
											// unless
											// accompanied by "/"
							nextCh();
							if (ch == '/') { // check for escape char "*/"
								break;
							}
						}
						nextCh();
					}
				} else if (ch == '/') {
					// CharReader maps all new lines to �\n�
					// Line Comment
					while (ch != '\n' && ch != EOFCH) {
						nextCh();
					}
				} else if (ch == '=') {
					nextCh();
					return new TokenInfo(DIV_ASSIGN, line); // Division
															// Assignment
				} else {
					nextCh();
					return new TokenInfo(DIV, line); // Division
				}
			} else {
				moreWhiteSpace = false;
			}
		}
		line = input.line();
		switch (ch) {
		case '(':
			nextCh();
			return new TokenInfo(LPAREN, line);
		case ')':
			nextCh();
			return new TokenInfo(RPAREN, line);
		case '{':
			nextCh();
			return new TokenInfo(LCURLY, line);
		case '}':
			nextCh();
			return new TokenInfo(RCURLY, line);
		case '[':
			nextCh();
			return new TokenInfo(LBRACK, line);
		case ']':
			nextCh();
			return new TokenInfo(RBRACK, line);
		case ';':
			nextCh();
			return new TokenInfo(SEMI, line);
		case ',':
			nextCh();
			return new TokenInfo(COMMA, line);
		case '=':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(EQUAL, line); // Equal
			} else {
				return new TokenInfo(ASSIGN, line); // Assign
			}
		case '!':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(LNOT_ASSIGN, line); // Logical NOT
															// Assignment
			} else {
				return new TokenInfo(LNOT, line); // Logical NOT
			}
		case '?':
			nextCh();
			return new TokenInfo(QMARK, line); // Ternary IF
		case ':':
			nextCh();
			return new TokenInfo(COLON, line); // Ternary ELSE
		case '*':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(STAR_ASSIGN, line); // Multiply Assignment
			} else {
				return new TokenInfo(STAR, line); // Multiply
			}
		case '%':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(MOD_ASSIGN, line); // Modulo Assignment
			} else {
				return new TokenInfo(MOD, line); // Modulo
			}
		case '+':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(PLUS_ASSIGN, line); // Plus Assignment
			} else if (ch == '+') {
				nextCh();
				return new TokenInfo(INC, line); // Increment
			} else {
				return new TokenInfo(PLUS, line); // Plus
			}
		case '-':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(MINUS_ASSIGN, line); // Minus Assignment
			} else if (ch == '-') {
				nextCh();
				return new TokenInfo(DEC, line); // Decrement
			} else {
				return new TokenInfo(MINUS, line); // Minus
			}
		case '&':
			nextCh();
			if (ch == '&') {
				nextCh();
				return new TokenInfo(LAND, line); // Logical AND
			} else {
				// reportScannerError("Operator & is not supported in j--.");
				// return getNextToken();
				return new TokenInfo(AND, line); // AND
			}
		case '^':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(XOR_ASSIGN, line); // Exclusive OR
														// Assignment
			} else {
				// reportScannerError("Operator & is not supported in j--.");
				// return getNextToken();
				return new TokenInfo(XOR, line); // Exclusive OR
			}
		case '>':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(GE, line); // Greater Than Assignment
			} else if (ch == '>') {
				nextCh();
				if (ch == '>') {
					return new TokenInfo(RS_ZF, line); // Right Shift Zero Fill
				} else {
					return new TokenInfo(RS, line); // Right Shift
				}
			} else {
				return new TokenInfo(GT, line); // Greater Than
			}
		case '<':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(LE, line); // Less Than Assignment
			} else if (ch == '<') {
				nextCh();
				if (ch == '=') {
					nextCh();
					return new TokenInfo(LS_ASSIGN, line); // Left Shift Assign
				} else {
					nextCh();
					return new TokenInfo(LS, line); // Binary Left Shift
				}
			} else {
				return new TokenInfo(LT, line); // Less Than
			}
		case '|':
			nextCh();
			if (ch == '=') {
				nextCh();
				return new TokenInfo(IOR_ASSIGN, line); // Inclusive OR and
														// assignment
			} else if (ch == '|') {
				nextCh();
				return new TokenInfo(LOR, line); // Logical OR
			} else {
				nextCh();
				return new TokenInfo(OR, line); // OR
			}
		case '\'':
			buffer = new StringBuffer();
			buffer.append('\'');
			nextCh();
			if (ch == '\\') {
				nextCh();
				buffer.append(escape());
			} else {
				buffer.append(ch);
				nextCh();
			}
			if (ch == '\'') {
				buffer.append('\'');
				nextCh();
				return new TokenInfo(CHAR_LITERAL, buffer.toString(), line);
			} else {
				// Expected a ' ; report error and try to
				// recover.
				reportScannerError(ch
						+ " found by scanner where closing ' was expected.");
				while (ch != '\'' && ch != ';' && ch != '\n') {
					nextCh();
				}
				return new TokenInfo(CHAR_LITERAL, buffer.toString(), line);
			}
		case '"':
			buffer = new StringBuffer();
			buffer.append("\"");
			nextCh();
			while (ch != '"' && ch != '\n' && ch != EOFCH) {
				if (ch == '\\') {
					nextCh();
					buffer.append(escape());
				} else {
					buffer.append(ch);
					nextCh();
				}
			}
			if (ch == '\n') {
				reportScannerError("Unexpected end of line found in String");
			} else if (ch == EOFCH) {
				reportScannerError("Unexpected end of file found in String");
			} else {
				// Scan the closing "
				nextCh();
				buffer.append("\"");
			}
			return new TokenInfo(STRING_LITERAL, buffer.toString(), line);

			// EDIT: Edited to account for double numbers
			// case '.':
			// nextCh();
			// return new TokenInfo(DOT, line);

		case EOFCH:
			return new TokenInfo(EOF, line);

		case '.':
		case '_':
		case '0':
			// Handle only simple decimal integers for now.
			// EDIT: Handles double now.
			// nextCh();
			// return new TokenInfo(INT_LITERAL, "0", line);
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':

			 /*
			 * =================================================================
			 * =        First Ch is a digit
			 * =================================================================
			 */
			if (isDigit(ch)) {
				buffer = new StringBuffer();
				// Handles the addition of whole numbers
				while (isDigit(ch)) {
					buffer.append(ch);
					nextCh();
					// Handles "_" within the whole numbers
					if (ch == '_') {
						buffer.append(ch);
						nextCh();
					}
					// Handles suffixes
					if (ch == 'd' | ch == 'f') {
						buffer.append(ch);
						nextCh();
						// Attempt to make error if there is anything beyond one suffix
						if (ch != ' ') {
							System.err.println("Error: Not a valid number");
							buffer.setLength(0); // Clears buffer
						}
					}
					// Handle Exponentials
					if (ch == 'E' | ch == 'e') {
						buffer.append(ch);
						nextCh();
						// Handle if user enters in positive or negative signs
						if (ch == '+' | ch == '-') {
							buffer.append(ch);
							nextCh();
							// Loops to get exponential number
							while (isDigit(ch)) {
								buffer.append(ch);
								nextCh();
							}
						}
					}
				}

				// Handles when number has decimal number
				if (ch == '.') {
					buffer.append(ch);
					nextCh();
					// Attempt to prevent multiple decimal points within one
					// buffer
					// Did not work
					if (ch == '.') {
						buffer.append(ch);
						nextCh();
						if (ch != ' ') {
							// I tried making code that will get the whole buffer so I can
							// error message it out, but did not work
							while (ch != EOFCH) {
								buffer.append(ch);
								nextCh();
								if (ch == ' ') {
									break;
								}
							}
						}
						System.err.println("Error: Not a valid number");
						buffer.setLength(0); // Clears buffer so it doesn't print it
					}
					// Handles "_" within the decimal number
					if (ch == '_') {
						while (ch != ' ') {
							buffer.append(ch);
							nextCh();
						}
						// Need an error message if "_" follows a "."
						System.err.println("Error: Not a valid number");
						buffer.setLength(0); // Clears buffer so it doesn't print it
					}
					// Handles if one decimal and digits follow after
						while (isDigit(ch)) {
							buffer.append(ch);
							nextCh();
							// checks for space holders
							if (ch == '_') {
								while (ch == '_') {
									nextCh();
								}
							}
							// suffix check
							if (ch == 'd' | ch == 'f') {
								buffer.append(ch);
								nextCh();
							}
							// check exponentials
							if (ch == 'E' | ch == 'e') {
								buffer.append(ch);
								nextCh();
								// check if signs before exponential
								if (ch == '+' | ch == '-') {
									buffer.append(ch);
									nextCh();
									while (isDigit(ch)) {
										buffer.append(ch);
										nextCh();
									}
								}
							}
						}
					return new TokenInfo(DOUBLE_LITERAL, buffer.toString(),
							line);
				}
				// return int literal if there is no decimal
				return new TokenInfo(INT_LITERAL, buffer.toString(), line);
			}

			 /*
			 * =================================================================
			 * =        First Ch is "_"
			 * =================================================================
			 */
			if (ch == '_') {
				buffer = new StringBuffer();
				buffer.append(ch);
				nextCh();
				while (ch != EOFCH) {
					buffer.append(ch);
					nextCh();
					if (ch == ' ') {
						break;
					}
				}
			}

			 /*
			 * =================================================================
			 * =        First Ch is "."
			 * =================================================================
			 */
			
			if (ch == '.') {
				buffer = new StringBuffer();
				buffer.append(ch);
				nextCh();
				if (!isDigit(ch)) {
					return new TokenInfo(DOT, line);
				}
				while (isDigit(ch)) {
					buffer.append(ch);
					nextCh();
				}
				if (ch == '.') {
					buffer.append(ch);
					nextCh();
					if (ch != ' ' || ch != '\t') {
						buffer.append(ch);
						while (ch != EOFCH) {
							buffer.append(ch);
							nextCh();
							if (ch == ' ') {
								break;
							}
						}
					}
					System.err.println("Error: Not a valid number");
					buffer.setLength(0); // Clears buffer
				}
				// Handles "_" within the decimal number
				while (ch == '_') {
					nextCh();
				}
				if (ch == 'd' | ch == 'f') {
					buffer.append(ch);
					nextCh();
				}
				if (ch == 'E' | ch == 'e') {
					buffer.append(ch);
					nextCh();
					if (ch == '+' | ch == '-') {
						buffer.append(ch);
						nextCh();
						while (isDigit(ch)) {
							buffer.append(ch);
							nextCh();
						}
					}
				}
				return new TokenInfo(DOUBLE_LITERAL, buffer.toString(), line);
			}

			// ======INITIAL CODE======
			// buffer = new StringBuffer();
			// while (isDigit(ch)) {
			// buffer.append(ch);
			// nextCh();
			// }
			// return new TokenInfo(INT_LITERAL, buffer.toString(), line);

		default:
			if (isIdentifierStart(ch)) {
				buffer = new StringBuffer();
				while (isIdentifierPart(ch)) {
					buffer.append(ch);
					nextCh();
				}
				String identifier = buffer.toString();
				if (reserved.containsKey(identifier)) {
					return new TokenInfo(reserved.get(identifier), line);
				} else {
					return new TokenInfo(IDENTIFIER, identifier, line);
				}
			} else {
				reportScannerError("Unidentified input token: '%c'", ch);
				nextCh();
				return getNextToken();
			}
		}
	}

	/**
	 * Scan and return an escaped character.
	 * 
	 * @return escaped character.
	 */

	private String escape() {
		switch (ch) {
		case 'b':
			nextCh();
			return "\\b";
		case 't':
			nextCh();
			return "\\t";
		case 'n':
			nextCh();
			return "\\n";
		case 'f':
			nextCh();
			return "\\f";
		case 'r':
			nextCh();
			return "\\r";
		case '"':
			nextCh();
			return "\"";
		case '\'':
			nextCh();
			return "\\'";
		case '\\':
			nextCh();
			return "\\\\";
		default:
			reportScannerError("Badly formed escape: \\%c", ch);
			nextCh();
			return "";
		}
	}

	/**
	 * Advance ch to the next character from input, and update the line number.
	 */

	private void nextCh() {
		line = input.line();
		try {
			ch = input.nextChar();
		} catch (Exception e) {
			reportScannerError("Unable to read characters from input");
		}
	}

	/**
	 * Report a lexcial error and record the fact that an error has occured.
	 * This fact can be ascertained from the Scanner by sending it an
	 * errorHasOccurred() message.
	 * 
	 * @param message
	 *            message identifying the error.
	 * @param args
	 *            related values.
	 */

	private void reportScannerError(String message, Object... args) {
		isInError = true;
		System.err.printf("%s:%d: ", fileName, line);
		System.err.printf(message, args);
		System.err.println();
	}

	/**
	 * Return true if the specified character is a digit (0-9); false otherwise.
	 * 
	 * @param c
	 *            character.
	 * @return true or false.
	 */

	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	/**
	 * Return true if the specified character is a whitespace; false otherwise.
	 * 
	 * @param c
	 *            character.
	 * @return true or false.
	 */

	private boolean isWhitespace(char c) {
		switch (c) {
		case ' ':
		case '\t':
		case '\n': // CharReader maps all new lines to '\n'
		case '\f':
			return true;
		}
		return false;
	}

	/**
	 * Return true if the specified character can start an identifier name;
	 * false otherwise.
	 * 
	 * @param c
	 *            character.
	 * @return true or false.
	 */

	private boolean isIdentifierStart(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '$');
	}

	/**
	 * Return true if the specified character can be part of an identifier name;
	 * false otherwise.
	 * 
	 * @param c
	 *            character.
	 * @return true or false.
	 */

	private boolean isIdentifierPart(char c) {
		return (isIdentifierStart(c) || isDigit(c));
	}

	/**
	 * Has an error occurred up to now in lexical analysis?
	 * 
	 * @return true or false.
	 */

	public boolean errorHasOccurred() {
		return isInError;
	}

	/**
	 * The name of the source file.
	 * 
	 * @return name of the source file.
	 */

	public String fileName() {
		return fileName;
	}

}

/**
 * A buffered character reader. Abstracts out differences between platforms,
 * mapping all new lines to '\n'. Also, keeps track of line numbers where the
 * first line is numbered 1.
 */

class CharReader {

	/** A representation of the end of file as a character. */
	public final static char EOFCH = (char) -1;

	/** The underlying reader records line numbers. */
	private LineNumberReader lineNumberReader;

	/** Name of the file that is being read. */
	private String fileName;

	/**
	 * Construct a CharReader from a file name.
	 * 
	 * @param fileName
	 *            the name of the input file.
	 * @exception FileNotFoundException
	 *                if the file is not found.
	 */

	public CharReader(String fileName) throws FileNotFoundException {
		lineNumberReader = new LineNumberReader(new FileReader(fileName));
		this.fileName = fileName;
	}

	/**
	 * Scan the next character.
	 * 
	 * @return the character scanned.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */

	public char nextChar() throws IOException {
		return (char) lineNumberReader.read();
	}

	/**
	 * The current line number in the source file, starting at 1.
	 * 
	 * @return the current line number.
	 */

	public int line() {
		// LineNumberReader counts lines from 0.
		return lineNumberReader.getLineNumber() + 1;
	}

	/**
	 * Return the file name.
	 * 
	 * @return the file name.
	 */

	public String fileName() {
		return fileName;
	}

	/**
	 * Close the file.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 */

	public void close() throws IOException {
		lineNumberReader.close();
	}

}
