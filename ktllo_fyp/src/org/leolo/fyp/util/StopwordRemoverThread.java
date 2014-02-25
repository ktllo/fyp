/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

import java.util.Arrays;
import org.leolo.fyp.StopwordList;

/**
 *
 * @author leolo
 */
public class StopwordRemoverThread extends Thread implements WordCounterUser{
    
    StringBuffer workspace;
    PostRanker ranker;
    int wordCount=-1;
    private int pointer=0;//Next character to be scan
    private int perviousPointer=0;//First character of current word
    
    public StopwordRemoverThread(PostRanker ranker,String string){
        this.ranker = ranker;
        workspace = new StringBuffer(string);
    }
    
    @Override
    public void run(){
        removeStopword();
        //Assume that the stopwords are removed
        String result = workspace.toString();
        WordCountThread wct = new WordCountThread(this, result);
        wct.start();
        //Await the WordCountThread finish its job
        while(true){
            if(this.wordCount >= 0){
                break;
            }
            synchronized(this){
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                   this.ranker.exception(ex);//Hand it back to its owner.
                }
            }
        }
        //Everything is ready
        synchronized(ranker){
            ranker.swrtReport(new StopwordRemoverResult(result,this.wordCount));
            ranker.notify();
        }
    }
    
    @Override
    public void wctReport(int value) {
        this.wordCount = value;
    }
    
    private CharacterBlock nextWord(){
        //TODO: Fill in pre-process code
        perviousPointer = pointer;
        CharacterType pervious = CharacterType.FIRST_CHARACTER;
        boolean haveChar = false;
        boolean wordFormed = false;
        CharacterType type = null;
        do{
            char currentChar = workspace.charAt(pointer);
            type = CharacterType.identify(currentChar);
            switch(type){
                case NUMBER:
                case FULLSTOP:
                case DELIMITER:
                    if(haveChar){
                        wordFormed = true;
                    }else{
                        perviousPointer++;
                    }
                    break;
                case LATIN:
                    haveChar = true;
                    if(pervious == CharacterType.CJK){
                        wordFormed = true;
                    }
                    break;
                case CJK:
                    haveChar = true;
                    if(pervious == CharacterType.LATIN){
                        wordFormed = true;
                    }
            }
            if(wordFormed){
                if(pervious==CharacterType.CJK){
                    return new CharacterBlock(workspace.substring(perviousPointer, pointer),KeywordType.CJK);
                }else{
                    return new CharacterBlock(workspace.substring(perviousPointer, pointer),KeywordType.LATIN);
                }
            }
            pervious = type;
            if(++pointer >= workspace.length()){
                if(type==CharacterType.CJK){
                    return new CharacterBlock(workspace.substring(perviousPointer, pointer),KeywordType.CJK);
                }else{
                    return new CharacterBlock(workspace.substring(perviousPointer, pointer),KeywordType.LATIN);
                }
            }
        }while( true );
        //return null;
    }
    
    private void removeWord(){
        this.workspace.delete(this.perviousPointer, pointer);
        //System.out.println("Removing "+perviousPointer+" to "+pointer);
        pointer=perviousPointer;
    }
    
    private void removeCharacter(int start,int end){
        if(start >= end) {
            throw new java.lang.IllegalArgumentException("end index must bigger than the start index.");
        }
        //System.out.println("Removing "+start+" to "+end);
        this.workspace.delete(start, end);
        pointer-=(end-start);
    }
    
    //Check is finish scaning the StringBuffer
    private boolean isEnd(){
        if( this.pointer >= this.workspace.length() ){
            return true;
        }
        return false;
    }
    
    public void removeStopword(){
        //Do some work to remove the stopword
        do{
            CharacterBlock curWord = this.nextWord();
            //System.out.print(curWord.data);
            //System.out.print(" is type "+curWord.type.name());
            if(curWord.type == KeywordType.LATIN){
                if(Arrays.binarySearch(StopwordList.ENGLISH, curWord.data, String.CASE_INSENSITIVE_ORDER) >= 0){ 
                    //Remove the stopword
                    this.removeWord();
                }
            }else{
                //Handle the non-english stopword, with different algroithm to handle them
                //TODO: Develop an algroithm to handle this case
            }
            //System.out.println();
        }while(!isEnd());
    }
    
    public String getString(){
        return workspace.toString();
    }
    
    class CharacterBlock{
        public String data;
        public KeywordType type;
        
        public CharacterBlock(String data,KeywordType type){
            this.data=data;
            this.type = type;
        }
    }
}
