package controller;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Post;
import model.User;
import model.dao.DAOFactory;
import model.dao.MySQLUserDAO;
import model.dao.PostDAO;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {"/post", "/post/save", "/post/update", "/post/delete"})
public class PostController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		System.out.println(action);

		switch (action) {
		case "/facebook/post": {
			
			loadUsers(req);

			RequestDispatcher rd = req.getRequestDispatcher("posts.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/post/save": {

			String postId = req.getParameter("postId");
			if (postId != null && !postId.equals(""))
				try {
					updatePost(req);
				} catch (ModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				try {
					insertPost(req);
				} catch (ModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			resp.sendRedirect("/facebook/post");			
			break;
		}
		case "/facebook/post/update": {

			loadPost(req);

			RequestDispatcher rd = req.getRequestDispatcher("/form_post.jsp");
			rd.forward(req, resp);
			break;
		} case "/facebook/post/delete": {
			
			deletePost(req);
			
			resp.sendRedirect("/facebook/post");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + action);
		}
	}

	private void deletePost(HttpServletRequest req) {
		String postIdString = req.getParameter("postId");
		int postId = Integer.parseInt(postIdString);
		
		Post post = new Post(postId);
		
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		try {
			dao.delete(post);
		} catch (ModelException e) {
			// log no servidor
			e.getCause().printStackTrace();
			e.printStackTrace();
		}
	}

	private void updatePost(HttpServletRequest req) throws ModelException {
		Post post = createPost(req);

		PostDAO dao = DAOFactory.createDAO(PostDAO.class);

		try {
			System.out.println(dao.update(post));
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
		}
		req.setAttribute("post_id", "");
	}

	private Post createPost(HttpServletRequest req) throws ModelException {
	    String postId = req.getParameter("post_id");
	    String postContent = req.getParameter("post_content");
	    Date postDate = new Date();
	    int id = Integer.parseInt(req.getParameter("user_id"));

	    UserDAO daoUser = DAOFactory.createDAO(UserDAO.class);
	    PostDAO daoPost = DAOFactory.createDAO(PostDAO.class);

	    Post post;
	    if (postId == null || postId.equals("")) {
	        post = new Post(); 
	    } else {
	        post = daoPost.findById(Integer.parseInt(postId)); 
	    }

	    post.setContent(postContent);
	    post.setPostDate(postDate);
	    post.setUser(daoUser.findById(id));

	    return post;
	}

	private void loadPost(HttpServletRequest req) {
	    String postIdParameter = req.getParameter("postId");

	    if (postIdParameter == null || postIdParameter.isEmpty()) {
	        System.out.println("Erro no ID.");
	        return;
	    }

	    try {
	        int postId = Integer.parseInt(postIdParameter);

	        PostDAO dao = DAOFactory.createDAO(PostDAO.class);
	        Post post = dao.findById(postId);

	        if (post == null) {
	            throw new ModelException("Post não encontrado para alteração");
	        }

	        req.setAttribute("post", post);
	    } catch (NumberFormatException e) {
	        System.out.println("Erro: postId inválido.");
	        e.printStackTrace();
	    } catch (ModelException e) {
	        e.printStackTrace();
	    }
	}

	private void insertPost(HttpServletRequest req) throws ModelException {
		Post post = createPost(req);

		PostDAO dao = DAOFactory.createDAO(PostDAO.class);

		try {
			dao.save(post);
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
		}
	}

	private void loadUsers(HttpServletRequest req) {
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		List<Post> posts = null;
		try {
			posts = dao.listAll();
		} catch (ModelException e) {
			e.printStackTrace();
		}
		if (posts != null)
			req.setAttribute("posts", posts); 
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

	    String action = req.getRequestURI();

	    if (action.equals("/facebook/post/save")) {
	        String postId = req.getParameter("post_id");
	        try {
	            if (postId != null && !postId.isEmpty()) {
	                updatePost(req);
	            } else {
	                insertPost(req);
	            }
	        } catch (ModelException e) {
	            e.printStackTrace();
	        }
	        resp.sendRedirect("/facebook/post");
	    } else {
	        doGet(req, resp); 
	    }
	}


}
