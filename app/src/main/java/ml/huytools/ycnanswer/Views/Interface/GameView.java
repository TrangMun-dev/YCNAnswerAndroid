package ml.huytools.ycnanswer.Views.Interface;

import ml.huytools.ycnanswer.Core.MVP.EntityManager;
import ml.huytools.ycnanswer.Models.Entities.ConfigQuestionEntity;
import ml.huytools.ycnanswer.Models.Entities.QuestionEntity;

public interface GameView {
    void visibleAll(boolean status);
    void showLoading();
    void updateTextLoading(String text);
    void hideLoading();
    void showMessage(String message);
    void setDataTableScore(EntityManager<ConfigQuestionEntity> configQuestionEntities);
    void showQuestion(QuestionEntity questionEntity);
    void close();
}
