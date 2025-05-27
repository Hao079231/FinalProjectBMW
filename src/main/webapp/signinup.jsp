<html>

<head>
    <title>Sign Up & Login Page</title>
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
        }

        .hidden {
            display: none;
        }
    </style>
</head>

<body class="bg-gray-100 flex flex-col items-center justify-center min-h-screen">
    <!-- Thông báo lỗi nếu có -->
    <% if(request.getAttribute("error") !=null) { %>
        <div id="error-toast" class="bg-red-600 text-white p-4 rounded-lg shadow-lg mb-6 flex items-center space-x-4">
            <span>
                <%= request.getAttribute("error") %>
            </span>
            <button onclick="closeToast('error-toast')"
                class="ml-4 bg-transparent text-white font-bold py-2 px-4 border border-white rounded">OK</button>
        </div>
        <% } %>

            <!-- Navigation Buttons -->
            <div class="flex space-x-4 mb-6">
                <a href="ProductList" class="bg-orange-500 text-white px-6 py-2 rounded-full">Home</a>
                <button id="show-login" class="bg-orange-500 text-white px-6 py-2 rounded-full">Login</button>
                <button id="show-signup" class="bg-orange-500 text-white px-6 py-2 rounded-full">Sign Up</button>
            </div>

            <div class="bg-white shadow-lg rounded-lg flex max-w-4xl w-full relative">
                <!-- Sign Up Form -->
                <div id="signup-form" class="w-1/2 p-8 hidden">
                    <h2 class="text-3xl font-bold mb-4">Sign Up For Client</h2>
                    <p class="text-gray-500 text-center mb-4">Use your personal details to create an account</p>
                    <form id="signupForm" action="SignInUp" method="POST" data-no-csrf="true">
                        <input type="hidden" name="action" value="signup">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <input type="text" name="username" placeholder="User"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <input type="password" name="password" placeholder="Password"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <input type="text" name="email" placeholder="Email"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <input type="text" name="address" placeholder="Address"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <input type="text" name="phone" placeholder="Phone"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <button type="submit" class="w-full bg-orange-500 text-white py-2 rounded">SIGN UP</button>
                    </form>
                </div>

                <!-- Sign In Form -->
                <div id="login-form" class="w-1/2 p-8">
                    <h2 class="text-3xl font-bold mb-4">Sign In</h2>
                    <form id="loginForm" action="SignInUp" method="POST" data-no-csrf="true">
                        <input type="hidden" name="action" value="signin">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <input type="text" name="username" placeholder="User"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <input type="password" name="password" placeholder="Password"
                            class="w-full p-2 mb-4 border border-gray-300 rounded">
                        <button type="submit" class="w-full bg-orange-500 text-white py-2 rounded">SIGN IN</button>
                        <div class="text-center mt-4">
                            <a href="forgot_password.jsp" class="text-orange-500 hover:text-orange-700">Forgot password?</a>
                        </div>
                    </form>
                </div>

                <!-- Right Side Content -->
                <div
                    class="w-1/2 bg-gradient-to-r from-pink-500 to-red-500 text-white p-8 flex flex-col justify-center items-center rounded-r-lg">
                    <h2 class="text-3xl font-bold mb-4">Hello, Friend!</h2>
                    <p class="mb-4 text-center">Welcome to our website</p>
                </div>
            </div>

            <script>
                const loginForm = document.getElementById('login-form');
                const signupForm = document.getElementById('signup-form');
                const showLoginButton = document.getElementById('show-login');
                const showSignupButton = document.getElementById('show-signup');
                const loginFormElement = document.getElementById('loginForm');
                const signupFormElement = document.getElementById('signupForm');

                showLoginButton.addEventListener('click', () => {
                    loginForm.classList.remove('hidden');
                    signupForm.classList.add('hidden');
                });

                showSignupButton.addEventListener('click', () => {
                    signupForm.classList.remove('hidden');
                    loginForm.classList.add('hidden');
                });
            </script>
</body>

</html>