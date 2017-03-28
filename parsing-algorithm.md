# Parsing Algorithm

    String words = sentence.spit(" ");
    int i = 0;
    String previousLexicalEntry;
    Sltag curr;
    while (i < words.length) {
        String lexicalEntry = "";
        List<Sltag> candidates = null;
        List<Sltag> temp = null;
        do {
            if (i >= words.lenght) {
                throw new Exception("lexicalEntry not found");
            }
            candidates = temp;
            lexicalEntry = lexicalEntry.concat(((lexicalEntry.isEmpty()) ? "": " ") + words[i++];
        } while (!(temp=grammar.get(lexicalEntry)).isEmpty() || grammar.isThere(lexicalEntry));
        
        if (candidates.size() > 1) {
            for (Sltag candidate : candidates) {
                if (i == 0 && candidate.hasLeftSub()) {
                    candidates.remove(candidate);
                } else if (candidate.isAdjunctable()) {
                    waitingList.add(ADJ, candidate, previousLexicalEntry);
                } else {
                    waitingList.add(SUB; candidate);
                }
            }
        } else {
            Sltag candidate = candidates.get(0);
            if (candidate.isAdjunctable()) {
                adjunctions.add(candidate, previousLexicalEntry);
            } else if (candidate.isSentence()) {
                if (curr != null) throw new Exception("Cannot decide sentence root: multiple root found.");
                curr = candidate;
            } else {
                substitutions.add(candidate);
            }
        }        
        previousLexicalEntry = lexicalEntry;
    }
    
    if (waitingList.isEmpty()) {
        return curr;
    } else {
        for (int i = 0; i < waitingList.size(); i++) {
            for (WaitingNode n : waitingList.get(i, SUB) {
                if (execSub(curr, n)) {
                    waitingList.remove(n);
                    break;
                }
            }
            for (WaitingNode n : waitingList.get(i, SUB) {
                if (execAdj(curr, n)) {
                    waitingList.remove(n);
                    break;
                }
            }
        }
    }