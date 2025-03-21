package androidx.constraintlayout.core.parser;

import androidx.core.location.LocationRequestCompat;
import java.io.PrintStream;

public class CLElement {
    protected static int BASE_INDENT = 2;
    protected static int MAX_LINE = 80;
    protected long end = LocationRequestCompat.PASSIVE_INTERVAL;
    private int line;
    protected CLContainer mContainer;
    private final char[] mContent;
    protected long start = -1;

    public CLElement(char[] content) {
        this.mContent = content;
    }

    public boolean notStarted() {
        return this.start == -1;
    }

    public void setLine(int line2) {
        this.line = line2;
    }

    public int getLine() {
        return this.line;
    }

    public void setStart(long start2) {
        this.start = start2;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end2) {
        if (this.end == LocationRequestCompat.PASSIVE_INTERVAL) {
            this.end = end2;
            if (CLParser.DEBUG) {
                PrintStream printStream = System.out;
                printStream.println("closing " + hashCode() + " -> " + this);
            }
            CLContainer cLContainer = this.mContainer;
            if (cLContainer != null) {
                cLContainer.add(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addIndent(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append(' ');
        }
    }

    public String toString() {
        long j = this.start;
        long j2 = this.end;
        if (j > j2 || j2 == LocationRequestCompat.PASSIVE_INTERVAL) {
            return getClass() + " (INVALID, " + this.start + "-" + this.end + ")";
        }
        String content = new String(this.mContent).substring((int) this.start, ((int) this.end) + 1);
        return getStrClass() + " (" + this.start + " : " + this.end + ") <<" + content + ">>";
    }

    /* access modifiers changed from: protected */
    public String getStrClass() {
        String myClass = getClass().toString();
        return myClass.substring(myClass.lastIndexOf(46) + 1);
    }

    /* access modifiers changed from: protected */
    public String getDebugName() {
        if (!CLParser.DEBUG) {
            return "";
        }
        return getStrClass() + " -> ";
    }

    public String content() {
        String content = new String(this.mContent);
        long j = this.end;
        if (j != LocationRequestCompat.PASSIVE_INTERVAL) {
            long j2 = this.start;
            if (j >= j2) {
                return content.substring((int) j2, ((int) j) + 1);
            }
        }
        long j3 = this.start;
        return content.substring((int) j3, ((int) j3) + 1);
    }

    public boolean isDone() {
        return this.end != LocationRequestCompat.PASSIVE_INTERVAL;
    }

    public void setContainer(CLContainer element) {
        this.mContainer = element;
    }

    public CLElement getContainer() {
        return this.mContainer;
    }

    public boolean isStarted() {
        return this.start > -1;
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        return "";
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        return "";
    }

    public int getInt() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getInt();
        }
        return 0;
    }

    public float getFloat() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getFloat();
        }
        return Float.NaN;
    }
}
