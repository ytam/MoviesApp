
package io.github.ytam.mvp.model.series;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.ytam.mvp.model.search.ResultsItem;

public class SeriesModel {

	@SerializedName("page") private int page;

	@SerializedName("total_pages") private int totalPages;

	@SerializedName("results") private List<ResultsItem> results;

	@SerializedName("total_results") private int totalResults;

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
	public String toString(){
		return
				"SearchModel{" +
						"page = '" + page + '\'' +
						",total_pages = '" + totalPages + '\'' +
						",results = '" + results + '\'' +
						",total_results = '" + totalResults + '\'' +
						"}";
	}
}