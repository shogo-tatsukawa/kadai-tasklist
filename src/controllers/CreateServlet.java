package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // new.jspから送られてきた「_token」を格納
        String _token = request.getParameter("_token");
        // CSRF対策の確認
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            // Taskのインスタンス作成
            Task t = new Task();

            // content, created_at, updated_atをセット
            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreated_at(currentTime);
            t.setUpdated_at(currentTime);

            // データベースに保存、コミット
            em.persist(t);
            em.getTransaction().commit();
            em.close();

            // indexページにリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
