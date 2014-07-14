package ch.epfl.modularity.datastructure;

public class RankingTableValue {
	public float similarityValue;
	public float correspondenceValue;

	public RankingTableValue(float similarityValue,
			float correspondenceValue) {
		this.similarityValue = similarityValue;
		this.correspondenceValue = correspondenceValue;
	}

	public RankingTableValue() {
		this(-1, -1);
	}

	public boolean isSimilarityAppear() {
		return similarityValue >= 0;
	}

	public boolean isCorrespondenceAppear() {
		return correspondenceValue >= 0;
	}
}
