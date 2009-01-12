import sun.text.normalizer.NormalizerImpl;




public class Test {

	public static void main(String[] args) {
		String s="èàìùòčšžđ";
		s=NormalizerImpl.canonicalDecomposeWithSingleQuotation(s).replaceAll( "\\p{InCombiningDiacriticalMarks}+", "" );
		
		System.out.println(s);
	}

}
