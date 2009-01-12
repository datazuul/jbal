import sun.text.NormalizerImpl;


public class Test {

	public static void main(String[] args) {
		String s="èàòçéù";
		s=NormalizerImpl.canonicalDecomposeWithSingleQuotation(s).replaceAll( "\\p{InCombiningDiacriticalMarks}+", "" );
		
		System.out.println(s);
	}

}
