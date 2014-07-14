package ch.epfl.modularity.datastructure;


public class SMPair implements Comparable<SMPair> {
	
	
	private BaseObject srcObj;
	private BaseObject desObj;
	private RankingTableValue value;
	
	
	public SMPair(BaseObject srcSch, BaseObject desSch, RankingTableValue value){
		this.srcObj = srcSch;
		this.desObj = desSch;
		this.value = value;
		
	}
	
	public boolean isCorrespondenceAppear(){
		return value.correspondenceValue >= 0;
	}
	
	public boolean isSimilarityAppear(){
		return value.similarityValue >= 0;
	}

	public float getSimilarityValue() {
		return value.similarityValue;
	}

	public void setSimilarityValue(float similarityValue) {
		this.value.similarityValue = similarityValue;
	}

	public float getCorrespondenceValue() {
		return value.correspondenceValue;
	}

	public void setCorrespondenceValue(float correctpondenceValue) {
		this.value.correspondenceValue = correctpondenceValue;
	}

	public BaseObject getSrc() {
		return srcObj;
	}

	public BaseObject getDes() {
		return desObj;
	}
	
	public boolean isSameDes(BaseObject schema){
		if(desObj.getName().equals(schema.getName())){
			return true;
		}
		else{
			return false;
		}
	}

	
	public String toString(){
		return String.format("%s\t%s\t%6.2f\t%6.2f", srcObj.getFullName(), desObj.getFullName(), value.similarityValue, value.correspondenceValue);
	}
	

	@Override
	public int compareTo(SMPair p) {
		return srcObj.getFullName().compareTo(p.srcObj.getFullName()) * 1000 + desObj.getFullName().compareTo(p.desObj.getFullName());
	}
	
	
}
