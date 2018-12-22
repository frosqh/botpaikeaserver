package com.frosqh.botpaikeaserver.ts3api.spellchecker;

public interface LevenshteinDistance {

    public static int getDistance(String word1, String word2){

        Integer[][] dist = new Integer[word1.length()][word2.length()];
        int i;
        int j;
        int subCost;

        for(i=0;i<word1.length();i++){
            dist[i][0]=i;
        }
        for (j=0;j<word2.length();j++){
            dist[0][j]=j;
        }
        for (i=1;i<word1.length();i++){
            for (j=1;j<word2.length();j++){
                if (word1.charAt(i-1) == word2.charAt(j-1))
                    subCost = 0;
                else
                    subCost = 1;
                dist[i][j] = Math.min(Math.min(dist[i-1][j]+1,dist[i][j-1]+1),dist[i-1][j-1]+subCost);
            }
        }
        return dist[word1.length()-1][word2.length()-1];
    }
}
