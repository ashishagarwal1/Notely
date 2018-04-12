package com.example.ashishaggarwal.notely.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Selection;
import android.text.style.UnderlineSpan;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A generic undo/redo implementation for TextViews.
 */
//https://gist.github.com/zeleven/0cfa738c1e8b65b23ff7df1fc30c9f7e#file-usage-java-L18
public class TextViewUndo {

    /**
     * Is undo/redo being performed? This member signals if an undo/redo
     * operation is currently being performed. Changes in the text during
     * undo/redo are not recorded because it would mess up the undo history.
     */
    private boolean mIsUndoOrRedo = false;

    /**
     * The edit history.
     */
    private EditHistory mEditHistory;

    private CharSequence mBeforeChange;

    private CharSequence mAfterChange;


    // =================================================================== //

    public TextViewUndo() {
        mEditHistory = new EditHistory();
    }

    /**
     * Set the maximum history size. If size is negative, then history size is
     * only limited by the device memory.
     */
    public void setMaxHistorySize(int maxHistorySize) {
        mEditHistory.setMaxHistorySize(maxHistorySize);
    }


    public EditHistory getmEditHistory() {
        return mEditHistory;
    }

    public void setmEditHistory(EditHistory mEditHistory) {
        this.mEditHistory = mEditHistory;
    }

    /**
     * Clear history.
     */
    public void clearHistory() {
        mEditHistory.clear();
    }

    /**
     * Can undo be performed?
     */
    public boolean getCanUndo() {
        return (mEditHistory.mmPosition > 0);
    }

    public int getPreviousResId() {
        EditItem edit = mEditHistory.checkPrevious();
        if (edit == null) {
            return -1;
        }
        return mEditHistory.checkPrevious().mmResId;
    }

    /**
     * Perform undo.
     */
    public void undo(Editable text) {
        EditItem edit = mEditHistory.getPrevious();
        if (edit == null) {
            return;
        }
        int start = edit.mmStart;
        int end = start + (edit.mmAfter != null ? edit.mmAfter.length() : 0);

        mIsUndoOrRedo = true;
        text.replace(start, end, edit.mmBefore);
        mIsUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0, text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text, edit.mmBefore == null ? start
                : (start + edit.mmBefore.length()));
    }

    /**
     * Keeps track of all the edit history of a text.
     */
    public final class EditHistory implements Parcelable{

        /**
         * The position from which an EditItem will be retrieved when getNext()
         * is called. If getPrevious() has not been called, this has the same
         * value as mmHistory.size().
         */
        private int mmPosition = 0;

        /**
         * Maximum undo history size.
         */
        private int mmMaxHistorySize = -1;

        /**
         * The list of edits in chronological order.
         */
        private LinkedList<EditItem> mmHistory = new LinkedList<>();

        public EditHistory(Parcel source) {
            source.readList(mmHistory, null);
            this.mmMaxHistorySize = source.readInt();
            this.mmPosition = source.readInt();
        }

        public EditHistory() {
        }

        /**
         * Clear history.
         */
        private void clear() {
            mmPosition = 0;
            mmHistory.clear();
        }

        /**
         * Adds a new edit operation to the history at the current position. If
         * executed after a call to getPrevious() removes all the future history
         * (elements with positions >= current history position).
         */
        private void add(EditItem item) {
            while (mmHistory.size() > mmPosition) {
                mmHistory.removeLast();
            }
            mmHistory.add(item);
            mmPosition++;

            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Set the maximum history size. If size is negative, then history size
         * is only limited by the device memory.
         */
        private void setMaxHistorySize(int maxHistorySize) {
            mmMaxHistorySize = maxHistorySize;
            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Trim history when it exceeds max history size.
         */
        private void trimHistory() {
            while (mmHistory.size() > mmMaxHistorySize) {
                mmHistory.removeFirst();
                mmPosition--;
            }

            if (mmPosition < 0) {
                mmPosition = 0;
            }
        }

        /**
         * Traverses the history backward by one position, returns and item at
         * that position.
         */
        private EditItem getPrevious() {
            if (mmPosition == 0) {
                return null;
            }
            mmPosition--;
            return mmHistory.get(mmPosition);
        }

        private EditItem checkPrevious() {
            if (mmPosition == 0) {
                return null;
            }
            return mmHistory.get(mmPosition-1);
        }


        /**
         * Traverses the history forward by one position, returns and item at
         * that position.
         */
        private EditItem getNext() {
            if (mmPosition >= mmHistory.size()) {
                return null;
            }
            EditItem item = mmHistory.get(mmPosition);
            mmPosition++;
            return item;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        public final Parcelable.Creator CREATOR = new Parcelable.Creator() {

            @Override
            public EditHistory createFromParcel(Parcel source) {
                return new EditHistory(source);
            }

            @Override
            public EditHistory[] newArray(int size) {
                return new EditHistory[size];
            }
        };
    }

    /**
     * Represents the changes performed by a single edit operation.
     */
    private final class EditItem implements Parcelable{
        private final int mmResId;
        private final int mmStart;
        private final CharSequence mmBefore;
        private final CharSequence mmAfter;

        /**
         * Constructs EditItem of a modification that was applied at position
         * start and replaced CharSequence before with CharSequence after.
         */
        public EditItem(int start, CharSequence before, CharSequence after, int resId) {
            mmResId = resId;
            mmStart = start;
            mmBefore = before;
            mmAfter = after;
        }

        public EditItem(Parcel source) {
            mmResId = source.readInt();
            mmStart = source.readInt();
            mmBefore = source.readString();
            mmAfter = source.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        public final Parcelable.Creator CREATOR = new Parcelable.Creator() {

            @Override
            public EditItem createFromParcel(Parcel source) {
                return new EditItem(source);
            }

            @Override
            public EditItem[] newArray(int size) {
                return new EditItem[size];
            }
        };
    }


    public void beforeTextChanged(CharSequence s, int start, int count) {
        if (mIsUndoOrRedo) {
            return;
        }
        mBeforeChange = s.subSequence(start, start + count);
    }

    public void onTextChanged(CharSequence s, int start,
                              int count, int resId) {
        if (mIsUndoOrRedo) {
            return;
        }
        mAfterChange = s.subSequence(start, start + count);
        mEditHistory.add(new EditItem(start, mBeforeChange, mAfterChange, resId));
    }
}
