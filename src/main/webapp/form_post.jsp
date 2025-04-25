<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <title>Cadastro de Post</title>
</head>

<body>
    <div class="container">
        <div class="row">
            <div class="col-2"></div>

            <form action="${pageContext.request.contextPath}/post/save" method="GET" class="col-8">
                <h1>Cadastro de Post</h1>

                <input type="hidden" id="post_id" name="post_id" value="${post.id}"/>

                <div class="mb-3">
                    <label for="post_content_id" class="form-label">Conteúdo</label>
                    <textarea id="post_content_id" name="post_content" class="form-control" rows="5" required>${post.content}</textarea>
                </div>

                    <div class="mb-3">
                        <label class="form-label">Usuário</label>
                        <input type="text" class="form-control" value="${post.user.name}" name="user_id">
                    </div>

                <button type="submit" class="btn btn-primary">Salvar</button>
            </form>

            <div class="col-2"></div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>

</html>
