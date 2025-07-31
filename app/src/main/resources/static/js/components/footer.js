/*
  Function to dynamically render the footer content on the page.
  Ensures consistency across all views without duplicating HTML manually.
*/

function renderFooter() {
  // 1. Select the <footer> element from the DOM
  const footer = document.getElementById("footer");

  // 2. Inject structured footer content using innerHTML
  footer.innerHTML = `
    <footer class="footer"> <!-- 2. Footer wrapper for overall layout -->
      <div class="footer-container"> <!-- 3. Container for alignment and spacing -->

        <!-- 4. Logo and copyright -->
        <div class="footer-logo">
          <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo">
          <p>© Copyright 2025. All Rights Reserved by Hospital CMS.</p>
        </div>

        <!-- 5. Footer Links Section (organized in 3 columns) -->
        <div class="footer-links">

          <!-- 6. Company Links -->
          <div class="footer-column">
            <h4>Company</h4>
            <a href="#">About</a>
            <a href="#">Careers</a>
            <a href="#">Press</a>
          </div>

          <!-- 7. Support Links -->
          <div class="footer-column">
            <h4>Support</h4>
            <a href="#">Account</a>
            <a href="#">Help Center</a>
            <a href="#">Contact Us</a>
          </div>

          <!-- 8. Legal Links -->
          <div class="footer-column">
            <h4>Legals</h4>
            <a href="#">Terms & Conditions</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Licensing</a>
          </div>

        </div> <!-- 9. End of footer-links -->
      </div> <!-- 9. End of footer-container -->
    </footer> <!-- 10. End of footer wrapper -->
  `;
}

// 11. Call the function to render the footer on page load
renderFooter();
