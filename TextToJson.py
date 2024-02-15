
import re
import json
import os

def process_text_file(file_path):
    print("OKKKK");
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # Assuming titles are between double square brackets
    title_pattern = r'\[\[(.*?)\]\]'
    titles = re.findall(title_pattern, content)

    # Initialize variables
    json_data = []
    current_title_index = 0

    # Iterate through the titles and extract content
    for title in titles:
        start_index = content.find(f"[[{title}]]") + len(f"[[{title}]]")
        if current_title_index < len(titles) - 1:
            end_index = content.find(f"[[{titles[current_title_index + 1]}]]")
            section_content = content[start_index:end_index].strip()
        else:
            # For the last title, extract content until the end of the file
            section_content = content[start_index:].strip()

        # Find categories between "CATEGORIES:" and the next title
        categories_pattern = r'CATEGORIES:(.*?)(?=\n\n|$)'
        categories_match = re.search(categories_pattern, section_content, re.DOTALL)
        if categories_match:
            section_categories = categories_match.group(1).strip()
        else:
            section_categories = ""

        # Create a JSON entry for the section
        section_data = {"title": title, "content": section_content.rstrip(), "category": section_categories.lstrip()}
        json_data.append(section_data)

        # Move to the next title
        current_title_index += 1

    return json_data
        
def process_text_files(directory_path, output_json_prefix, max_file_size_kb=5000):
    json_data = []
    current_json_size = 0
    file_number = 1

    # Get a list of all text files in the directory
    files = [f for f in os.listdir(directory_path) if os.path.isfile(os.path.join(directory_path, f)) and f.endswith('.txt')]

    for file_name in files:
        file_path = os.path.join(directory_path, file_name)
        pages_data = process_text_file(file_path)
        json_data.extend(pages_data)

        # Write the JSON data to a file when it reaches the size limit
        current_json_size += len(json.dumps(pages_data, ensure_ascii=False, indent=2))
        if current_json_size > max_file_size_kb * 1024:
            output_json_path = f"{output_json_prefix}_{file_number}.json"
            with open(output_json_path, 'w', encoding='utf-8') as json_file:
                json.dump(json_data, json_file, ensure_ascii=False, indent=2)
            file_number += 1
            json_data = []
            current_json_size = 0

    # Write the final JSON data to the last file
    output_json_path = f"{output_json_prefix}_{file_number}.json"
    with open(output_json_path, 'w', encoding='utf-8') as json_file:
        json.dump(json_data, json_file, ensure_ascii=False, indent=2)

    print(f"Processed {len(files)} files and saved results to {output_json_path}")

input_directory = r"C:\Daniela\Master\An1\Data Mining\wiki-subset-20140602.tar"
output_json_file = r"C:\Daniela\Master\An1\Data Mining\TextToJson\pages_final\pages.json"
process_text_files(input_directory, output_json_file)
