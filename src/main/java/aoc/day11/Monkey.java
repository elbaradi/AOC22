package aoc.day11;

import java.util.ArrayList;
import java.util.List;

public class Monkey {
  private List<Long> items = new ArrayList<>();
  private Operation operator;
  private String term;
  private Long test;
  private Integer fail;
  private Integer success;

  private Monkey monkeyFail;

  private Monkey monkeySuccess;

  private Integer inspectionCount = 0;

  public void takeTurn(boolean worryIsManageable, long effectiveMaxWorry) {
    items.forEach( item -> {

      long worry = inspect(item, worryIsManageable);
      worry %= effectiveMaxWorry;
      if (worry % test == 0)
        monkeySuccess.getItems().add(worry);
      else
        monkeyFail.getItems().add(worry);
    });

    items.clear();
  }

  private Long inspect(Long item, boolean worryIsManageable) {
    if (term.equals("old"))
      item = operator.apply(item, item);
    else
      item = operator.apply(item, Long.parseLong(term));

    if (worryIsManageable)
      item /= 3;

    inspectionCount++;

    return item;
  }

  public void setItems(List<Long> items) {
    this.items = items;
  }

  public void setOperator(Operation operator) {
    this.operator = operator;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public void setTest(Long test) {
    this.test = test;
  }

  public void setFail(Integer fail) {
    this.fail = fail;
  }

  public void setSuccess(Integer success) {
    this.success = success;
  }

  public void setMonkeyFail(Monkey monkeyFail) {
    this.monkeyFail = monkeyFail;
  }

  public void setMonkeySuccess(Monkey monkeySuccess) {
    this.monkeySuccess = monkeySuccess;
  }

  public List<Long> getItems() {
    return items;
  }

  public Long getTest() {
    return test;
  }

  public Integer getFail() {
    return fail;
  }

  public Integer getSuccess() {
    return success;
  }

  public Integer getInspectionCount() {
    return inspectionCount;
  }

}
