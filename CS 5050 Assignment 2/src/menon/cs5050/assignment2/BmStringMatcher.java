package menon.cs5050.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BmStringMatcher extends StringMatcher {
	
	private Map<Character, Integer> badMatchShiftTable;
	private int[] goodSuffixShiftTable;
	
	BmStringMatcher(String text, String pattern) {
		
		super(text, pattern);
		
		this.badMatchShiftTable = new HashMap<Character, Integer>(pattern.length());
		populateBadMatchShiftTable();
		
		this.goodSuffixShiftTable = new int[pattern.length()];
		populateGoodSuffixShiftTable();
		
	}
	
	/**
	 * Populate the bad match character table
	 */
	private void populateBadMatchShiftTable() {
		
		int patternLength = getPattern().length();
		Character badMatchTableKey = null;
		Integer badMatchTableValue = null;
		
		for (int patternIndex = 1; patternIndex < patternLength - 1; ++patternIndex) {
		
			badMatchTableKey = Character.valueOf(Character.toLowerCase(getPattern().charAt(patternIndex)));
			badMatchTableValue = patternIndex;		
			badMatchShiftTable.put(badMatchTableKey, badMatchTableValue);
		}
	}
	
	/**
	 * Populate the good suffix shift table
	 */
	private void populateGoodSuffixShiftTable() {
		
		int patternLength = getPattern().length(), startIndex = 0;
		String patternSuffix = null;
		
		//Start at the end of the pattern and for every preceding index, find the rightmost position of an occurrence of the substring that starts from
		//the current index, that is not preceded by the character to the left of the said substring.
		for (int patternIndex = patternLength - 1; patternIndex > 0; --patternIndex) {
			
			//Get the current substring
			patternSuffix = getPattern().substring(patternIndex);
			
			//Now find the rightmost occurrence of this substring
			for (int patternSuffixMatcherIndex = patternIndex -1; patternSuffixMatcherIndex >= 0 ; --patternSuffixMatcherIndex) {
			
				startIndex = patternSuffixMatcherIndex - patternSuffix.length() + 1;
				if (startIndex < 0) {
					break;
				}
			
				if (getPattern().substring(startIndex, patternSuffixMatcherIndex + 1).equals(patternSuffix)) {
					if (startIndex > 0) {
						if (Character.toLowerCase(getPattern().charAt(patternIndex -1)) != Character.toLowerCase(getPattern().charAt(startIndex -1))) {
							this.goodSuffixShiftTable[patternIndex] = patternSuffixMatcherIndex;
							break;
						}
					} else {
						this.goodSuffixShiftTable[patternIndex] = patternSuffixMatcherIndex;
						break;
					}
				}
			
			}
		
		}
		
	}
	
	/**
	 * @param mismatchedCharacter
	 * @return the greater of the two values of the bad character match shift and the good suffix shift
	 */
	private int getPatternShiftValue(char mismatchedCharacter, int mismatchIndex) {
		
		Character mismatchValue = Character.valueOf(Character.toLowerCase(mismatchedCharacter));
		int badMatchShift = 0, goodSuffixShift = 0, patternLength = getPattern().length();
		
		if (this.badMatchShiftTable.containsKey(mismatchValue)) {
			badMatchShift = Math.max(1, mismatchIndex - this.badMatchShiftTable.get(mismatchValue).intValue());
		} else {
			badMatchShift = 1;
		}
		
		goodSuffixShift = this.goodSuffixShiftTable[mismatchIndex];
		
		return Math.max(badMatchShift, goodSuffixShift);
			
	}

	@Override
	public List<Integer> match() {
		return null;
	}

}