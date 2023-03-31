const btnShowPassword = document.querySelector(".login-form-pass-hide");
const btnHidePassword = document.querySelector(".login-form-pass-show");


btnShowPassword.addEventListener("click", () => {
  const password = document.querySelector("#password");
  password.type = "text";
  btnShowPassword.classList.add("hidden");
  btnHidePassword.classList.remove("hidden");
});

btnHidePassword.addEventListener("click", () => {
  const password = document.querySelector("#password");
  password.type = "password";
  btnShowPassword.classList.remove("hidden");
  btnHidePassword.classList.add("hidden");
});