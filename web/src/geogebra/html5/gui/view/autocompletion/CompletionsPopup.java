package geogebra.html5.gui.view.autocompletion;

import geogebra.html5.gui.inputfield.AutoCompleteTextFieldW;
import geogebra.html5.gui.view.algebra.RadioButtonTreeItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CompletionsPopup extends MultiWordSuggestOracle {

	private AutoCompleteTextFieldW textField;
	private RadioButtonTreeItem rbtInterface;
	private VerticalPanel list;

	public CompletionsPopup() {
		super();
		clear();
    }

	private void registerListeners() {
		// TODO Auto-generated method stub
	    
    }

	public void addTextField(AutoCompleteTextFieldW autoCompleteTextField) {
		this.textField = autoCompleteTextField;
		if (autoCompleteTextField != null)
			this.rbtInterface = null;
    }

	public void addMathQuillGGBField(RadioButtonTreeItem mqggb) {
		this.rbtInterface = mqggb;
		if (mqggb != null)
			this.textField = null;
	}

	public void showHistoryCompletions(ArrayList<String> history) {
		if (history != null) {
			clear();
			addAll(history);
		}
    }
	
	@Override
	public void requestSuggestions(Request request, Callback callback) {

		if (textField == null && rbtInterface == null)
			return;

		if (textField != null && !textField.getAutoComplete()) {
			callback.onSuggestionsReady(request, new Response(Collections.EMPTY_LIST));
			return;
		} else if (rbtInterface != null && !rbtInterface.getAutoComplete()) {
			callback.onSuggestionsReady(request, new Response(
			        Collections.EMPTY_LIST));
			return;
		}
		String query;
		List<String> completions;
		if (textField != null) {
			textField.resetCompletions();
			query = request.getQuery();
			completions = textField.getCompletions();
		} else {
			rbtInterface.resetCompletions();
			query = request.getQuery();
			completions = rbtInterface.getCompletions();
		}

		if (completions == null || completions.size() == 0) {
			callback.onSuggestionsReady(request, new Response(Collections.EMPTY_LIST));
			return;
		}
		int limit = request.getLimit();

		// respect limit for number of choices
		int numberTruncated = Math.max(0, completions.size() - limit);
		if (limit < completions.size()) {
			completions = completions.subList(0, limit);
		}

		// convert candidates to suggestions
		List<MultiWordSuggestion> suggestions = convertToFormattedSuggestions(query, completions);

		Response response = new Response(suggestions);
		response.setMoreSuggestionsCount(numberTruncated);

		callback.onSuggestionsReady(request, response);
	}
	
	private static List<MultiWordSuggestion> convertToFormattedSuggestions(String query, List<String> candidates) {
		List<MultiWordSuggestion> suggestions = new ArrayList<MultiWordSuggestion>();
		for (int i = 0; i < candidates.size(); i++) {
			String candidate = candidates.get(i);
			
			SafeHtmlBuilder accum = new SafeHtmlBuilder();
			if (query.length() < candidate.length()) {
				String part1 = candidate.substring(0, query.length());
				String part2 = candidate.substring(query.length(),
				        candidate.length());
				accum.appendHtmlConstant("<strong>");
				accum.appendEscaped(part1);
				accum.appendHtmlConstant("</strong>");
				accum.appendEscaped(part2);
			} else {
				accum.appendEscaped(candidate);
			}
			suggestions.add(new MultiWordSuggestion(candidate, accum.toSafeHtml().asString()));
		}
		return suggestions;
	}
}
