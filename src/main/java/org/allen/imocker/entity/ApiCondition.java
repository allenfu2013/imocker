package org.allen.imocker.entity;

public class ApiCondition {

    private Integer id;

    private Integer apiInfoId;

    private String condKey;

    private String condType;

    private String condExpression;

    private String condValue;

    private String mockRetValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApiInfoId() {
        return apiInfoId;
    }

    public void setApiInfoId(Integer apiInfoId) {
        this.apiInfoId = apiInfoId;
    }

    public String getCondKey() {
        return condKey;
    }

    public void setCondKey(String condKey) {
        this.condKey = condKey;
    }

    public String getCondType() {
        return condType;
    }

    public void setCondType(String condType) {
        this.condType = condType;
    }

    public String getCondExpression() {
        return condExpression;
    }

    public void setCondExpression(String condExpression) {
        this.condExpression = condExpression;
    }

    public String getCondValue() {
        return condValue;
    }

    public void setCondValue(String condValue) {
        this.condValue = condValue;
    }

    public String getMockRetValue() {
        return mockRetValue;
    }

    public void setMockRetValue(String mockRetValue) {
        this.mockRetValue = mockRetValue;
    }

    @Override
    public String toString() {
        return "ApiCondition{" +
                "id=" + id +
                ", apiInfoId=" + apiInfoId +
                ", condKey='" + condKey + '\'' +
                ", condType='" + condType + '\'' +
                ", condExpression='" + condExpression + '\'' +
                ", condValue='" + condValue + '\'' +
                ", mockRetValue='" + mockRetValue + '\'' +
                '}';
    }
}
