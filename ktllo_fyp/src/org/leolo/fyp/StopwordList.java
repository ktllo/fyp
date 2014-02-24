package org.leolo.fyp;
/*
 * The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL
 * NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED",  "MAY", and
 * "OPTIONAL" in this document are to be interpreted as described in
 * RFC 2119.
 */
public class StopwordList {
    /**
     * A list of stopword in English. This list MUST be sorted. The entry listed here
     * are case insensitive.
     */
	public static final String [] ENGLISH = {"a",
		"ab",
		"able",
		"about",
                "and",
		"Yes",
		"ZZZZ"};
	/**
         * @deprecated Used in demo earlier.
         */
        public static final String [] ANOTHER = {"相片","影片"};//For demo only. deprecated.

        /**
         * An 2-D array storing the keyword in CHINESE. The stopword  list will 
         * split into different array according to it length, into the first 
         * dimension. The sub-array with index 1 contain those single word
         * stopword, sub-array with index 2 contain those 2 word stopword, and
         * so on. If there are no stopword in a subarray it MUST be empty,
         * unless there are no stopword longer than the said length, in such 
         * case, the subarray SHOULD NOT exist.
         * 
         * In each sub-array, the stopword MUST be encoded in UTF-8, and being
         * sorted according to its natrual order.
         */
	public static final String  CHINESE [][]={ {},//Length 0, should be EMPTY, but still leave here to simpify the code
					{"相","片","影"},//Length 1
					{"相片"}//Length 2
					}; 
}
