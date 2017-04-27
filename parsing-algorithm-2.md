# PARSING ALGORITHM

## PARSING

```
ASK_TRIGGERS = [do,does,did,is,are,was,were]

function isAskSentence(sentence):
  return ASK_TRIGGERS.contains(sentence.firstWord)

function parse(grammar, sentence):
  dashboard = new Dashboard()
  wlist = new WaitingList()
  words = sentence.split(" ")
  tokenized = [false] * words.len
  currLexicalEntry = null
  prevLexicalEntry = null
  curr = null
  i = 0

  isAskType = isAskSentence(sentence)

  while i < words.len:
    if tokenized[i]:
      i++
      continue
    currLexicalEntry = ""
    candidates = []

    itemp = i
    tempLexicalEntry = ""
    temp = []

    while itemp < words.len:
      tempLexicalEntry = concat(tempLexicalEntry,words[itemp++])
      temp = grammar.getAllMatchingESLTAG(tempLexicalEntry)
      if !temp.empty():
        i = itemp
        currLexicalEntry = tempLexicalEntry
        candidates += temp
      if !grammar.match(tempLexicalEntry):
        break

    if candidates.empty():
      throw Excpetion

    if candidates.size > 1:
      for candidate in candidates:
        if i == 0 && candidate.isLeftSub():
          candidates.remove(candidate)
        elif candidate.isAdjunctable():
          wlist.addAdjunction(candidate,prevLexicalEntry)
          candidates.remove(candidate)
        else:
          wlist.addSubstitution(candidate,prevLexicalEntry)
          candidates.remove(candidate)

    if candidates.size == 1:
      candidate = candidates.get(0)
      if candidate.isAdjunctable():
        dashboard.addAdjunction(candidate,prevLexicalEntry)
      elif candidate.isSentence():
        if curr != null:
          throw Exception
        curr = candidate
      else:
        dashboard.addSubstitution(candidate)

    prevLexicalEntry = currLexicalEntry

    if curr != null:
      localSubstitutions = curr.getDFS(SUB)
        for localSub in localSubstitutions:
          substituted = false
          waitingSubstitutions = dashboard.waitingSubstitutions()
          for waitingSub in waitingSubstitutions:
            if localSub.category == waitingSub.category:
              curr.substitute(waitingSub, localSub)
              waitingSubstitutions.remove(waitingSub)
              substituted = true
              localSubstitutions = curr.getNodesDFS(LtagNodeMarker.SUB).iterator();
              break
      waitingAdjunctions = dashboard.getAdjunctions()
      for waitingAdjunction in waitingAdjunctions:
        anchor = curr.firstMatch(waitingAdjunction.root.category, start)
        if anchor != null:
          curr.adjunct(waitingAdjunction, anchor)
          waitingAdjunctions.remove(waitingAdjunction)

  if not curr:
    throw Error

  for conflict in wlist.conflicts:
    for sub in conflict.substitutions:
      used = trySubstitution(curr,sub)
      if used:
        break

    if used:
      conflicts.remove(conflict)
      continue

    for adj in conflict.adjunctions:
      used = tryAdjunction(curr,adj)

  if isAskType:
    curr.semantics.select = false

  return curr
```


## TOKENIZER

```
buffer = [(w1,false,true),...,(wn,false,true)] // (word,tokenized,tokenizable)

function hasNext():
  return nextTokenizable() != -1

function next():
  isConsuming = false
  startConsuming = 0
  returnFirstFull = false

  start = nextTokenizable()

  if start == -1:
    return null

  end = start
  lexicalPattern = ""
  tmpLexicalPattern = ""
  candidates = []

  while end < buffer.size():
    elem = buffer.get(end)
    if !elem.isTokenized():
      tempLexicalPattern = tempLexicalPattern + elem.word()
      matchType = grammar.matchType(tempLexicalPattern)

      if matchType == FULL:
        candidates = []
        candidates += grammar.getAllMatchingESLTAG(tempLexicalPattern)
        lexicalPattern = tmpLexicalPattern
        buffer.get(end).isTokenized = true
        if returnFirstFull:
          break
      elif matchType == PART:
        buffer.get(end).isTokenized = true
      elif matchType == PART_STAR:
        isConsuming = true;
        startConsuming = end;
      elif matchtype == NONE:
        break
      else:
        if end == buffer.size() - 1
          // Consumption interrupted at: end with word elem.word()
          isConsuming = false;
          end = startConsuming - 1;
          returnFirstFull = true;
          // Index rolled back to: end
        else:
          // consumption continued at: end with word elem.word()
        }
    end++

  prevLexicalEntry = (start > 0) ? start - 1: null;

  return (lexicalPattern, candidates, prevLexicalEntry)

function nextTokenizable():
  for b in buffer:
    if !b.isTokenized() && b.isTokenizable():
      return b.indexOf(b)
  return -1

function reset():
  buffer = [(w1,false,true),...,(wn,false,true)]
```
