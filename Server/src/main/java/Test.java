import com.alibaba.fastjson.JSON;

public class Test
{
    public int getA()
    {
        return a;
    }

    public void setA(int a)
    {
        this.a = a;
    }

    private int a = 1;
    private String b = "testb";

    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }
}