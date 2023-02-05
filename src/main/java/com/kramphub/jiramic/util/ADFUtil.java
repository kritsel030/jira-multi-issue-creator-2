package com.kramphub.jiramic.util;

import com.kramphub.jiramic.domain.atlassian.adf.*;

// utilities for Atlassian Document Format
// https://developer.atlassian.com/cloud/jira/platform/apis/document/structure/
public class ADFUtil {

    /* textarea can have minimal mark down content:
       * line separators are treated as paragraph separators
       * each line which starts and ends with '+' is treated as a bold paragraph
       * each line which starts with '* ' is treated as an unnumbered list item
       * each line which starts with '# ' is treated as a numbered list item

      not supported:
       * multi level lists
       * bold words within a line (only the total line can be bold)
       * bold list items (list items are never bold)
     */
    public static Document textAreaToDocument(String textAreaContents) {
        String[] lines = textAreaContents.split(System.lineSeparator());

        Document document = new Document();
        boolean inList = false;
        for (String line : lines) {
            line = line.trim();
            if (!inList) {
               if (line.startsWith("* ")) {
                    document.addContentNode(createBulletListNode());
                    document.getLastContentNode().addContentNode(createListItemNode(line.substring(2)));
                    inList = true;
                } else if (line.startsWith("# ")) {
                    document.addContentNode(createOrderedListNode());
                    document.getLastContentNode().addContentNode(createListItemNode(line.substring(2)));
                    inList = true;
                } else {
                   document.addContentNode(processNonItemLine(line));
               }
            } else {
                // inList = true: last line we processed was a list item
                if (line.startsWith("* ") || line.startsWith("# ")) {
                    // the current line is a list item as well
                    document.getLastContentNode().addContentNode(createListItemNode(line.substring(2)));
                } else {
                    // the current line is not a list item, so we need to indicate we're no longer in a list
                    inList = false;
                    document.addContentNode(processNonItemLine(line));
                }
            }
        }
        // add the tagline
        document.addContentNode(createEmptyParagraphNode());
        BlockNode tagline = createEmptyParagraphNode()
            .addContentNode(createItalicTextNode("This story was created by KrampHub's "))
            .addContentNode(createItalicLinkNode("Jira multi-issue creator", "https://bitbucket.org/kramphub/jira-multi-issue-creator", "Jira multi-issue creator"))
            .addContentNode(createItalicTextNode("."));
        document.addContentNode(tagline);
        if (document.getContent() != null && document.getContent().size() != 0) {
            return document;
        } else {
            return null;
        }
    }

    // markup markings inspired by https://jira.atlassian.com/secure/WikiRendererHelpAction.jspa?section=all
    private static BlockNode processNonItemLine(String line) {
        BlockNode newNode;
        if (line.equals("")) {
            newNode = createEmptyParagraphNode();
        } else if (line.startsWith("*") && line.endsWith("*")) {
            newNode = createBoldParagraphNode(line.substring(1, line.length() - 1));
        } else if (line.startsWith("_") && line.endsWith("_")) {
            newNode = createItalicParagraphNode(line.substring(1, line.length() - 1));
        } else if (line.startsWith("+") && line.endsWith("+")) {
            newNode = createUnderlineParagraphNode(line.substring(1, line.length() - 1));
        } else {
            newNode = createParagraphNode(line);
        }
        return newNode;
    }

    private static BlockNode createParagraphNode(String text) {
        return new BlockNode(NodeType.BLOCK_PARAGRAPH).addContentNode(new InlineNode(NodeType.INLINE_TEXT, text));
    }

    private static BlockNode createBoldParagraphNode(String text) {
        return new BlockNode(NodeType.BLOCK_PARAGRAPH).addContentNode(new InlineNode(NodeType.INLINE_TEXT, text).addMark(new Mark(MarkType.BOLD)));
    }

    private static BlockNode createItalicParagraphNode(String text) {
        return new BlockNode(NodeType.BLOCK_PARAGRAPH).addContentNode(new InlineNode(NodeType.INLINE_TEXT, text).addMark(new Mark(MarkType.ITALIC)));
    }

    private static BlockNode createUnderlineParagraphNode(String text) {
        return new BlockNode(NodeType.BLOCK_PARAGRAPH).addContentNode(new InlineNode(NodeType.INLINE_TEXT, text).addMark(new Mark(MarkType.UNDERLINE)));
    }

    private static BlockNode createHardBreakParagraphNode() {
        return new BlockNode(NodeType.BLOCK_PARAGRAPH).addContentNode(new TypeOnlyInlineNode(NodeType.INLINE_HARD_BREAK));
    }

    private static BlockNode createOrderedListNode() {
        return new BlockNode(NodeType.BLOCK_ORDERED_LIST);
    }

    private static BlockNode createBulletListNode() {
        return new BlockNode(NodeType.BLOCK_BULLET_LIST);
    }

    private static BlockNode createListItemNode(String text) {
        return new BlockNode(NodeType.BLOCK_LIST_ITEM).addContentNode(createParagraphNode(text));
    }

    private static InlineNode createTextNode(String text) {
        return new InlineNode(NodeType.INLINE_TEXT, text);
    }

    private static InlineNode createItalicTextNode(String text) {
        return new InlineNode(NodeType.INLINE_TEXT, text).addMark(new Mark(MarkType.ITALIC));
    }

    private static InlineNode createLinkNode(String text, String href, String title) {
        return new InlineNode(NodeType.INLINE_TEXT, text).addMark(new Mark(MarkType.LINK).addAttr(MarkAttrName.HREF, href).addAttr(MarkAttrName.TITLE, title));
    }

    private static InlineNode createItalicLinkNode(String text, String href, String title) {
        return new InlineNode(NodeType.INLINE_TEXT, text)
                .addMark(new Mark(MarkType.ITALIC))
                .addMark(new Mark(MarkType.LINK).addAttr(MarkAttrName.HREF, href).addAttr(MarkAttrName.TITLE, title));
    }

    private static BlockNode createEmptyParagraphNode() {
        return new BlockNode(NodeType.BLOCK_PARAGRAPH);
    }
}
