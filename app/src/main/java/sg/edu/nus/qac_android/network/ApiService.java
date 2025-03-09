package sg.edu.nus.qac_android.network;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sg.edu.nus.qac_android.data.entity.Question;
import sg.edu.nus.qac_android.data.entity.Answer;

public interface ApiService {

    // 获取所有问题
    @GET("QnA/question")
    Call<List<Question>> getQuestions();

    // 提交新问题
    @POST("QnA/question/add")
    Call<Question> createQuestion(@Body Question question);

    // 获取某个问题的所有回答
    @GET("QnA/answers/by-question/{questionId}")
    Call<List<Answer>> getAnswersByQuestionId(@Path("questionId") UUID questionId);

    // 提交答案
    @POST("QnA/answers/create")
    Call<Answer> submitAnswer(@Body Answer answer);
}
