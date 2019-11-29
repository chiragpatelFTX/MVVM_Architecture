// ALTERed device_settings table to add new columns for device settings
UPDATE question_table set question_text = "Are you currently taking Ace Inhibitors (examples include Enalapril / Ramipril / Lisinopril / Perindopril)" where question_id=3
UPDATE question_table set question_text = "Are you currently taking Angiotensin Receptor Antagonist (examples include Losartan / Olmesartan / Telmisartan / Valsartan)" where question_id=4

