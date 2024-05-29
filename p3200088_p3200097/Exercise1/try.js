fetch('https://wiki-ads.onrender.com/categories')
    .then(res => res.json())
    .then(categoriesData => {
        const catList = document.getElementById('catList');

        fetch('https://wiki-ads.onrender.com/subcategories')
            .then(res => res.json())
            .then(subcategoriesData => {
                const catTemplate = document.getElementById('catTemplate').innerHTML;
                const template = Handlebars.compile(catTemplate);

                categoriesData.forEach(category => {
                    const li = document.createElement('li');
                    const sub = subcategoriesData.filter(sub => sub.category_id === category.id);

                    const imageUrl = `https://wiki-ads.onrender.com/${category.img.url}`;

                    const html = template({ ...category, sub, imageUrl });
                    li.innerHTML = html;
                    catList.appendChild(li);
                });
            })
            .catch(error => console.log('ERROR fetching subcategories'));
    })
    .catch(error => console.log('ERROR fetching categories'));
