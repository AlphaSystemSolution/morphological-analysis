# Highlight Processing Logic

This document explains the logic used to apply markup around highlighted portions of a text row.

## Purpose

The highlight-processing logic takes:

- the original text,
- a list of highlight ranges,
- token metadata for the text,
- and an accumulated result string,

then produces a final string where selected portions of the text are wrapped with markup.

The output is intended to preserve the original text order while inserting markup around the requested highlighted ranges.

## Inputs

### Text

The main input is a single text string, for example:

```plain text
word1 word2 word3
```


The text is split into tokens using spaces.

Each token is assigned a 1-based token position for highlight references.

For example:

| Token index | Token text |
|---:|---|
| 1 | `word1` |
| 2 | `word2` |
| 3 | `word3` |

## Highlight Range

A highlight range contains:

- a start token,
- an end token,
- an optional color.

Each token reference may include:

- a token index,
- an optional character location inside that token.

Conceptually:

```scala
Highlight(
  tokenStart = Token(tokenIndex = 2, locationIndex = Some(1)),
  tokenEnd = Token(tokenIndex = 3, locationIndex = Some(4)),
  color = Some("red")
)
```


This means:

> Start highlighting at character 1 of token 2, and end highlighting at character 4 of token 3.

## Location Index Rules

Token indexes are 1-based.

Location indexes are also treated as 1-based.

If the start location is missing, it defaults to the beginning of the start token:

```plain text
locationIndex = 1
```


If the end location is missing, it defaults to the end of the end token.

So this:

```scala
Highlight(
  Token(2, None),
  Token(3, None)
)
```


means:

> Highlight from the beginning of token 2 through the end of token 3.

## Absolute Offset Calculation

The most important simplification is to convert token-based positions into absolute string offsets.

For example:

```plain text
alpha beta gamma
```


The characters are positioned like this:

```plain text
alpha beta gamma
0123456789012345
```


Tokens:

| Token index | Token text | Start offset | End offset |
|---:|---|---:|---:|
| 1 | `alpha` | 0 | 5 |
| 2 | `beta` | 6 | 10 |
| 3 | `gamma` | 11 | 16 |

The spaces between tokens count as one character each.

To find the absolute offset for a token reference:

1. Count the lengths of all tokens before the target token.
2. Add the number of spaces before the target token.
3. Add the location inside the target token.
4. Adjust for whether the position is a start or end offset.

For a start offset, the 1-based location is converted to a 0-based string offset.

For an end offset, the location represents the character position after which highlighting ends.

## Splitting the Text

Once the start and end offsets are known, the text can be split into three parts:

```scala
val beforeHighlight = text.substring(0, startOffset)
val highlightedText = text.substring(startOffset, endOffset)
val afterHighlight = text.substring(endOffset)
```


For example:

```plain text
alpha beta gamma
```


Highlight from token 2 to token 2:

```plain text
beforeHighlight = "alpha "
highlightedText = "beta"
afterHighlight = " gamma"
```


Then markup is added only around `highlightedText`.

## Markup Rules

There are two kinds of markup:

### Default Markup

If no color is supplied, default markup is used:

```plain text
##highlighted text##
```


### Colored Markup

If a color is supplied, the start marker includes that color:

```plain text
[color]##highlighted text##
```


The end marker remains the default markup:

```plain text
##
```


So with color `red`, the highlighted output becomes:

```plain text
[red]##highlighted text##
```


## Processing Order

Highlights are processed from right to left.

This is important because the function progressively removes the already-processed suffix from the original text and prepends formatted pieces to the accumulated result.

For each highlight:

1. Convert the start token reference to a start offset.
2. Convert the end token reference to an end offset.
3. Split the current text into:
   - text before the highlight,
   - highlighted text,
   - text after the highlight.
4. Encode the highlighted and non-highlighted pieces.
5. Add markup around the highlighted piece.
6. Prepend the formatted pieces to the accumulated result.
7. Continue processing the remaining text before the current highlight.

Processing from right to left avoids having to adjust future offsets after markup is inserted.

## Example

Original text:

```plain text
alpha beta gamma delta
```


Highlight:

```plain text
token 2 through token 3
```


That means:

```plain text
beta gamma
```


The text is split as:

```plain text
beforeHighlight = "alpha "
highlightedText = "beta gamma"
afterHighlight = " delta"
```


The result becomes:

```plain text
alpha ##beta gamma## delta
```


If the highlight has color `red`, the result becomes:

```plain text
alpha [red]##beta gamma## delta
```


## Why This Approach Is Simpler

The simplified slicing logic is easier to reason about because it separates the problem into two clear steps:

1. Convert token-based highlight ranges into absolute offsets.
2. Slice the text using those offsets.

This avoids repeated manual calculations throughout the function and makes the intent clearer:

```scala
val startOffset = tokenOffset(tokenStart, tokenInfos)
val endOffset = tokenOffset(tokenEnd, tokenInfos, endOffset = true)
```


After that, the rest of the logic works with ordinary string slicing.