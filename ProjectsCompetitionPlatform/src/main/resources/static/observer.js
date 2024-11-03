window.onload = () => {
  const observer = new IntersectionObserver((items) => {
      items.forEach(item => {
        if (item.isIntersecting) {
          item.target.classList.add('in_view');
          item.target.classList.remove('not_in_view');
        }
      });
    }, {
      rootMargin: '0px',
      threshold: [0, 0.1, 1],
    },
  );

  const blocks = document.querySelectorAll('.page-block');
  blocks.forEach(block => {
    block.classList.add('not_in_view');
    observer.observe(block);
  });
};