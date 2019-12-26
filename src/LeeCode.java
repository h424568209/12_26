import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 单调栈: 1、 单调递增栈：数据的出栈顺序是单调递增的
 *        2、 单调递减栈：数据的出栈顺序是单调递减的
 */
public class LeeCode {
    /**
     * 移除无效的括号
     * 使用栈存储“（”，使用数组存储要删除的下标。 遍历删除数组，得到的就是结果
     * @param s 字符串
     * @return 移除无效的括号后的字符串
     */
    public String minRemoveToMakeValid(String s) {
        Stack<Integer> stack = new Stack<>();
        boolean []arr = new boolean[s.length()];
        char a [] = s.toCharArray();
        for(int i = 0 ; i < a.length;  i++){
            char c = a[i];
            if(a[i] == '('){
                stack.push(i);
            }else if(a[i] == ')'){
                if(!stack.isEmpty()){
                    stack.pop();
                }else{
                    arr[i] = true;
                }
            }
            }
        while(!stack.isEmpty()){
            arr[stack.pop()] = false;
        }
        StringBuffer  sb = new StringBuffer();
        for(int i =0 ; i < a.length ; i++){
            if(!arr[i]){
                sb.append(a[i]);
            }
        }
        return sb.toString();
    }
    /**
     * 使用两个栈，一个栈存放当前的元素，一个栈存放当前元素对应的次数，两个一一对应。
     * 删除k个相同且连续的元素，可以多次进行删除，直到没有K个连续相同的元素为止， 返回这个字符串
     * @param s 字符串
     * @param k 个数
     * @return 删除后的字符串
     */
    public String removeDuplicates(String s, int k) {
        Stack<Character> characterStack = new Stack<>();
        Stack<Integer> timesstack = new Stack<>();
        int times = 0;
        for(int i =0 ; i < s.length() ; i++){
            char c = s.charAt(i);
            if(characterStack.isEmpty() || characterStack.peek() != c){
                characterStack.push(c);
                timesstack.push(1);
            }else if(characterStack.peek() == c && timesstack.peek() +1 == k){
                characterStack.pop();
                timesstack.pop();
            }else
                timesstack.push(timesstack.pop()+1);
        }
        StringBuffer sb = new StringBuffer();
        while(!characterStack.isEmpty()){
            int t = timesstack.pop();
           char ch =  characterStack.pop();
            while(t!=0){
                sb.append(ch);
                t--;
            }
        }
        return sb.reverse().toString();
    }

    /**
     * 使用数组，每找到k个相连的元素就进行重新在这个数组中查找k个相连的元素
     * 递归
     */
    public String removeDuplicates1(String s, int k) {
        char [] cs = s.toCharArray();
        char last = cs[0];
        int position = -1;
        int count =1 ;
        for(int i = 1; i <cs.length ; i++){
            if(last == cs[i]){
                count ++;
                if(count == k){
                    position = i ;
                    break;
                }
            }else {
                last = cs[i];
                count = 1;
            }
        }
        //循环结束，没有发现所需求的k个相连的想同元素
        if(position == -1){
            return s;
        }else{
            String ns = s.substring(0,position-k+1) + s.substring(position+1);
            return removeDuplicates1(ns,k);
        }
    }

    /**
     * 反转每对括号间的子串
     * 给出一个字符串 s（仅含有小写英文字母和括号）。
     * 注意，您的结果中 不应 包含任何括号。
     *
     * 使用栈保存的是括号的下标，在数组中进行反转，最后将数组转换成字符串
     *      刚刚开始用栈保存的是字符，最后发现字符的逆序后又得入栈，这样很大程度的增加了复杂度，而且代码看起来很混乱
     *      最后使用栈存放下标后 ，就变得简单多了。
     * @param s 字符串
     * @return 反转后的字符串
     */
    public String reverseParentheses(String s) {
      StringBuffer sb = new StringBuffer();
      char[]arr = s.toCharArray();
      Stack<Integer> stack = new Stack<>();
      for(int i = 0 ; i <arr.length ; i++){
          if(arr[i] =='('){
              stack.push(i);
          }
          if(arr[i] == ')'){
              reverse(arr,stack.pop()+1,i-1);
          }
      }
      for(int i = 0 ; i < arr.length ; i++){
          if(arr[i]!=')' && arr[i]!='('){
              sb.append(arr[i]);
          }
      }
      return sb.toString();
    }

    private void reverse(char[] arr, int left, int right) {
        while(right > left){
            char tmp = arr[left];
            arr[left] = arr[right];
            arr[right] = tmp;
            right --;
            left++;
        }
    }

    /**
     * 给你一个正整数数组 arr，考虑所有满足以下条件的二叉树：
     * 	每个节点都有 0 个或是 2 个子节点。
     * 	数组 arr 中的值与树的中序遍历中每个叶节点的值一一对应。（知识回顾：如果一个节点有 0 个子节点，那么该节点为叶节点。）
     * 	每个非叶节点的值等于其左子树和右子树中叶节点的最大值的乘积。
     * 在所有这样的二叉树中，返回每个非叶节点的值的最小可能总和。这个和的值是一个 32 位整数。
     * @param arr 数组
     * @return 叶值的最小代价生成树
     */
    public int mctFromLeafValues(int[] arr) {
        //构建一个从栈底单调递减的栈，遇到增加的就弹出相乘  ----- 单调递增栈
            Stack<Integer> stack = new Stack<>();
            //第一个节点先进来，让for里面的代码不冗余
            stack.push(arr[0]);
            int num  = 0 ;
            for(int i = 1;  i < arr.length ; i ++){
                //比最小的元素大了，要一直弹出，保证栈的单调性，直到栈的最小元素大于arr[i]
               while  (!stack.isEmpty() && arr[i] > stack.peek()){
                   //弹出之后的第二小的元素，是在stack中还是新来的这个arr[i]不知道，所以要比较一下
                   int min1 = stack.pop();
                   int min2;
                   if(!stack.isEmpty()){
                       min2 = Math.min(arr[i],stack.peek());
                   }else{
                       min2 = arr[i];
                   }
                     num += min1*min2;
               }
               stack.push(arr[i]);
            }
            //如果栈不空，剩下的叶子节点要乘起来
            while( stack.size() > 1){
                num += stack.pop() * stack.peek();
            }
            return num;
    }
    public int longestWPI(int[] hours) {
        return 0;
    }

    /**
     * 单调递增栈  表示入栈元素左边第一个比它大的元素
     * result对应的位置是src中对应下标第一个比当前元素小的数
     */
    private int[] getLeftMinNum(int[] src) {
        int []result = new int[src.length];
        Stack<Integer> monotoneStack = new Stack<>();
        for(int i = 0 ; i < src.length ; i++){
            if(monotoneStack.isEmpty()){
                monotoneStack.push(src[i]);
                result[i] = 0;
            }else{
                while(!monotoneStack.isEmpty() && src[i] < monotoneStack.peek()){
                    monotoneStack.pop();
                }
                if(!monotoneStack.isEmpty()){
                    result[i] = monotoneStack.peek();
                }else{
                    result[i] = -1;
                }
                monotoneStack.push(src[i]);
            }
        }
        return result;
    }


    public static void main(String[] args) {
           LeeCode l =  new LeeCode();
//        System.out.println(l.longestWPI(new int[]{9,9,6,0,9,9,6}));
//
//        System.out.println(Arrays.toString(l.getLeftMinNum(new int[]{6, 10, 3, 7, 4, 12})));
//        System.out.println(l.mctFromLeafValues(new int[]{6,2,4}));

        System.out.println(l.reverseParentheses("(ed(et(oc))el)"));

        System.out.println(l.removeDuplicates("deeedbbcccbdaa",3));

        System.out.println(l.minRemoveToMakeValid("lee(t(c)o)de)"));
    }
}
