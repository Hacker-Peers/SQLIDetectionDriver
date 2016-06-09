package org.hackerpeers.sqli.analyzer;

/**
 * Placeholder Exception used only to get a snapshot of the stack at the moment of a call.
 * Used instead of querying the stack because getting the stack itself is 5-6 times slower.
 * Stack will be process in the threads instead.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
class StackTraceSnapshot extends Exception {
}
