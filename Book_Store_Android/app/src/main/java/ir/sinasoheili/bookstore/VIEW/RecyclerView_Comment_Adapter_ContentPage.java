package ir.sinasoheili.bookstore.VIEW;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.sinasoheili.bookstore.MODEL.Comment;
import ir.sinasoheili.bookstore.MODEL.User;
import ir.sinasoheili.bookstore.PRESENTER.User_Api;
import ir.sinasoheili.bookstore.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerView_Comment_Adapter_ContentPage extends RecyclerView.Adapter<RecyclerView_Comment_Adapter_ContentPage.ListView_ViewHolder_ContentPage>
{
    private Context context;
    private ArrayList<Comment> comments;
    private boolean dialog_is_show = false;

    public RecyclerView_Comment_Adapter_ContentPage(Context context , ArrayList<Comment> comments)
    {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ListView_ViewHolder_ContentPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cotent_page_recycler_view_comment_item, null , false);
        ListView_ViewHolder_ContentPage holder = new ListView_ViewHolder_ContentPage(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListView_ViewHolder_ContentPage holder, int position)
    {
        holder.fill(comments.get(position));
    }

    @Override
    public int getItemCount()
    {
        return comments.size();
    }


    public class ListView_ViewHolder_ContentPage extends RecyclerView.ViewHolder
    {
        private TextView tv_user_name ;
        private TextView tv_date ;
        private TextView tv_content ;

        public ListView_ViewHolder_ContentPage(@NonNull View root_view)
        {
            super(root_view);

            this.tv_user_name = root_view.findViewById(R.id.tv_user_name_comment);
            this.tv_date = root_view.findViewById(R.id.tv_date_comment);
            this.tv_content = root_view.findViewById(R.id.tv_content_comment);
        }

        public void fill(Comment comment)
        {
            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(User_Api.base_url)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

            User_Api api = retrofit.create(User_Api.class);
            Call<User> call = api.get_user_info(comment.getUser_id());
            call.enqueue(new Callback<User>()
            {
                @Override
                public void onResponse(Call<User> call, Response<User> response)
                {
                    tv_user_name.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t)
                {
                    if(dialog_is_show == false)
                    {
                        show_error_dialog();
                    }
                }
            });

            tv_date.setText(comment.getDate());
            tv_content.setText(comment.getContent());
        }
    }

    private void show_error_dialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("مشکل در ارتباط با سرور");
        dialog.setMessage("امکان برقراری ارتباط با سرور وجود ندارد لطفا بعدا امتحان کنید!");
        dialog.setCancelable(false);
        dialog.setPositiveButton("باشه", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog_is_show = true;
    }
}
