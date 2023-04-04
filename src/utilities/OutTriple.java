package utilities;

public class OutTriple<F, S, T>
{
    protected F _first;
    protected S _second;
    protected T _third;
    
    public OutTriple(F first, S second, T third)
    {
        _first = first;
        _second = second;
        _third = third;
    }

    public OutTriple()
    {
        this(null, null, null);
    }
    
    public void set(F first, S second, T third)
    {
        _first = first;
        _second = second;
        _third = third;
    }
    
    public F getFirst() { return _first; }
    public S getSecond() { return _second; }
    public T getThird() { return _third; }
}
